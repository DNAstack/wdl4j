version 1.0

# Version              Date            Developer                         Comments
#---------------------------------------------------------------------------------
#     0.1        2018-12-12        Drew Thompson              initial development
#   0.1.1        2019-06-12        Drew Thompson                 idxstats bug fix
#     0.2        2019-07-31        Drew Thompson          move to WDL version 1.0,
#                                                         new resume mechanism,
#                                                         combined pipeline,
#                                                         amalgamate all code
#   0.2.1        2019-08-09        Drew Thompson             fix error in outputs
#     0.3        2019-08-14         Drew & Scott       updating software versions

workflow Align {
    input {
        String patient
        String sample
        Array[Int] lanes
        Array[Array[File]] fastqs
        Array[String] uuids
        String library
        String platform
        String platform_unit
        String center
        File reference
        File reference_index
        File reference_dict
        File reference_amb
        File reference_ann
        File reference_bwt
        File reference_sa
        File reference_pac
        File known_sites
        File known_sites_index
        File gatk_intervals
    }

    # Directories
    String log_dir = "wdl_logs+run_info"

    # Task names
    String BWAMem_task_name_base = "~{patient}.~{sample}.bwa"
    String Sort_task_name_base = "~{patient}.~{sample}.sort"
    scatter (lane in lanes) {
        String BWAMem_task_names = "~{BWAMem_task_name_base}.~{lane}"
        String Sort_task_names = "~{Sort_task_name_base}.~{lane}"
    }

    String MergeAligned_task_name = "~{patient}.~{sample}.merge_aligned"
    String MarkDuplicates_task_name = "~{patient}.~{sample}.markdup"
    String RealignerTargetCreator_task_name = "~{patient}.~{sample}.rtc"
    String IndelRealigner_task_name = "~{patient}.~{sample}.IndelRealign"
    String BaseRecalibrator_task_name = "~{patient}.~{sample}.BaseRecal"
    String ApplyBQSR_task_name = "~{patient}.~{sample}.ApplyBQSR"
    String MergeRecalibrated_task_name = "~{patient}.~{sample}.merge_recalibrated"
    String Idxstats_task_name = "~{patient}.~{sample}.idxstats"
    String Barchart_task_name = "~{patient}.~{sample}.barchart"
    String Flagstat_task_name = "~{patient}.~{sample}.flagstat"
    String DepthOfCoverage_task_name = "~{patient}.~{sample}.coverage"
    String CollectInsertSizeMetrics_task_name = "~{patient}.~{sample}.InsertSize"
    String FASTQC_task_name = "~{patient}.~{sample}.fastqc"
    Array[String] task_names = flatten([BWAMem_task_names, Sort_task_names, [MergeAligned_task_name, MarkDuplicates_task_name, RealignerTargetCreator_task_name, IndelRealigner_task_name, BaseRecalibrator_task_name, ApplyBQSR_task_name, MergeRecalibrated_task_name, Idxstats_task_name, Barchart_task_name, Flagstat_task_name, DepthOfCoverage_task_name, CollectInsertSizeMetrics_task_name, FASTQC_task_name]])

    scatter (lane in lanes) {

        String BWAMem_task_name = "~{BWAMem_task_name_base}.~{lane}"
        String BWAMem_output_bam = "~{sample}.~{lane}.bam"
        call BWAMem {
            input:
                id = uuids[lane],
                sample = sample,
                library = library,
                platform = platform,
                platform_unit = platform_unit,
                center = center,
                r1_fastq = fastqs[lane][0],
                r2_fastq = fastqs[lane][1],
                reference = reference,
                reference_index = reference_index,
                reference_dict = reference_dict,
                reference_amb = reference_amb,
                reference_ann = reference_ann,
                reference_bwt = reference_bwt,
                reference_sa = reference_sa,
                reference_pac = reference_pac,
                log_dir = log_dir,
                task_name = BWAMem_task_name,
                out_bam = BWAMem_output_bam
        }

        String Sort_task_name = "~{Sort_task_name_base}.~{lane}"
        String Sort_output_bam = "~{sample}.~{lane}.sorted.bam"
        String Sort_output_bai = "~{Sort_output_bam}.bai"
        call Sort {
            input:
                input_bam = BWAMem.output_bam,
                log_dir = log_dir,
                task_name = Sort_task_name,
                out_bam = Sort_output_bam,
                out_bai = Sort_output_bai
        }
    }

    Array[String] MergeAligned_input_bams = Sort.output_bam
    Array[String] MergeAligned_input_bais = Sort.output_bai
    String MergeAligned_output_bam = "~{sample}.bam"
    String MergeAligned_output_bai = "~{sample}.bai"
    call MergeAligned{
        input:
            input_bams = MergeAligned_input_bams,
            input_bais = MergeAligned_input_bais,
            reference = reference,
            reference_index = reference_index,
            reference_dict = reference_dict,
            log_dir = log_dir,
            task_name = MergeAligned_task_name,
            out_bam = MergeAligned_output_bam,
            out_bai = MergeAligned_output_bai
    }

    String MarkDuplicates_output_bam = "~{sample}.markdup.bam"
    String MarkDuplicates_output_bai = "~{MarkDuplicates_output_bam}.bai"
    call MarkDuplicates {
        input:
            input_bam = MergeAligned.output_bam,
            input_bai = MergeAligned.output_bai,
            log_dir = log_dir,
            task_name = MarkDuplicates_task_name,
            out_bam = MarkDuplicates_output_bam,
            out_bai = MarkDuplicates_output_bai
    }

    call createIntervals {
        input:
            reference_dict = reference_dict
    }

    String RealignerTargetCreator_output_intervals = "~{sample}.markdup.rtc.intervals"
    scatter (interval_list in createIntervals.sequence_grouping) {
        call RealignerTargetCreator {
            input:
                input_bam = MarkDuplicates.output_bam,
                input_bai = MarkDuplicates.output_bai,
                reference = reference,
                reference_index = reference_index,
                reference_dict = reference_dict,
                interval_list = interval_list,
                log_dir = log_dir,
                task_name = RealignerTargetCreator_task_name,
                out_intervals = RealignerTargetCreator_output_intervals
        }

        String IndelRealigner_output_bam = "~{sample}.markdup.indelrealigned.bam"
        String IndelRealigner_output_bai = "~{sample}.markdup.indelrealigned.bai"
        call IndelRealigner {
            input:
                input_bam = MarkDuplicates.output_bam,
                input_bai = MarkDuplicates.output_bai,
                reference = reference,
                reference_index = reference_index,
                reference_dict = reference_dict,
                target_intervals = RealignerTargetCreator.output_intervals,
                interval_list = interval_list,
                log_dir = log_dir,
                task_name = IndelRealigner_task_name,
                out_bam = IndelRealigner_output_bam,
                out_bai = IndelRealigner_output_bai
        }

        String BaseRecalibrator_output_table = "~{sample}.markdup.indelrealigned.baserecal.table"
        call BaseRecalibrator {
            input:
                input_bam = IndelRealigner.output_bam,
                input_bai = IndelRealigner.output_bai,
                reference = reference,
                reference_index = reference_index,
                reference_dict = reference_dict,
                known_sites = known_sites,
                known_sites_index = known_sites_index,
                interval_list = interval_list,
                log_dir = log_dir,
                task_name = BaseRecalibrator_task_name,
                out_table = BaseRecalibrator_output_table
        }

        String ApplyBQSR_output_bam = "~{sample}.realigned-recalibrated.bam"
        String ApplyBQSR_output_bai = "~{sample}.realigned-recalibrated.bai"
        call ApplyBQSR {
            input:
                input_bam = IndelRealigner.output_bam,
                input_bai = IndelRealigner.output_bai,
                recal_table = BaseRecalibrator.output_table,
                reference = reference,
                reference_index = reference_index,
                reference_dict = reference_dict,
                interval_list = interval_list,
                log_dir = log_dir,
                task_name = ApplyBQSR_task_name,
                out_bam = ApplyBQSR_output_bam,
                out_bai = ApplyBQSR_output_bai
        }
    }

    Array[String] MergeRecalibrated_input_bams = ApplyBQSR.output_bam
    Array[String] MergeRecalibrated_input_bais = ApplyBQSR.output_bai
    String MergeRecalibrated_output_bam = "~{sample}.realigned-recalibrated.bam"
    String MergeRecalibrated_output_bai = "~{sample}.realigned-recalibrated.bai"
    String MergeRecalibrated_output_md5 = "~{MergeRecalibrated_output_bam}.md5"
    call MergeRecalibrated {
        input:
            input_bams = MergeRecalibrated_input_bams,
            input_bais = MergeRecalibrated_input_bais,
            log_dir = log_dir,
            task_name = MergeRecalibrated_task_name,
            out_bam = MergeRecalibrated_output_bam,
            out_bai = MergeRecalibrated_output_bai,
            out_md5 = MergeRecalibrated_output_md5
    }

    String Idxstats_output_file = "~{MergeRecalibrated_output_bam}.idxstats"
    call Idxstats {
        input:
            bam = MergeRecalibrated.output_bam,
            bai = MergeRecalibrated.output_bai,
            log_dir = log_dir,
            task_name = Idxstats_task_name,
            out_file = Idxstats_output_file
    }

    String Barchart_output_pdf = "~{MergeRecalibrated_output_bam}.alignment.barchart.pdf"
    call Barchart {
        input:
            idx_stats = Idxstats.output_file,
            sample = sample,
            log_dir = log_dir,
            task_name = Barchart_task_name,
            out_pdf = Barchart_output_pdf
    }

    String Flagstat_output_file = "~{MergeRecalibrated_output_bam}.flagstat"
    call Flagstat {
        input:
            bam = MergeRecalibrated.output_bam,
            bai = MergeRecalibrated.output_bai,
            log_dir = log_dir,
            task_name = Flagstat_task_name,
            out_file = Flagstat_output_file
    }

    String MergeRecalibrated_output_bam_no_ext = sub(MergeRecalibrated_output_bam, ".bam$", "")
    String DepthOfCoverage_output_files_prefix = "~{MergeRecalibrated_output_bam_no_ext}.gatk.depthofcoverage"
    call DepthOfCoverage {
        input:
            bam = MergeRecalibrated.output_bam,
            bai = MergeRecalibrated.output_bai,
            reference = reference,
            reference_index = reference_index,
            reference_dict = reference_dict,
            gatk_intervals = gatk_intervals,
            log_dir = log_dir,
            task_name = DepthOfCoverage_task_name,
            out_prefix = DepthOfCoverage_output_files_prefix
    }

    String CollectInsertSizeMetrics_output_metrics = "~{MergeRecalibrated_output_bam_no_ext}.insertsizemetrics.txt"
    String CollectInsertSizeMetrics_output_histogram = "~{MergeRecalibrated_output_bam_no_ext}.insertsizemetrics.histogram.pdf"
    call CollectInsertSizeMetrics {
        input:
            bam = MergeRecalibrated.output_bam,
            bai = MergeRecalibrated.output_bai,
            log_dir = log_dir,
            task_name = CollectInsertSizeMetrics_task_name,
            out_metrics = CollectInsertSizeMetrics_output_metrics,
            out_histogram = CollectInsertSizeMetrics_output_histogram
    }

    String FASTQC_output_zip = "~{MergeRecalibrated_output_bam_no_ext}_fastqc.zip"
    String FASTQC_output_html = "~{MergeRecalibrated_output_bam_no_ext}_fastqc.html"
    call FASTQC {
        input:
            bam = MergeRecalibrated.output_bam,
            log_dir = log_dir,
            task_name = FASTQC_task_name,
            out_zip = FASTQC_output_zip,
            out_html = FASTQC_output_html
    }

    output {
        File bam = MergeRecalibrated.output_bam
        File bai = MergeRecalibrated.output_bai
        File bam_md5sum = MergeRecalibrated.output_md5
        File idxstats = Idxstats.output_file
        File barchart = Barchart.output_pdf
        File flagstat = Flagstat.output_file
        Array [File] coverage_files = DepthOfCoverage.output_files
        File insert_size_metrics = CollectInsertSizeMetrics.output_metrics
        File insert_size_histogram = CollectInsertSizeMetrics.output_histogram
        File fastqc_zip = FASTQC.output_zip
        File fastqc_html = FASTQC.output_html
    }
}

task BWAMem {
    input {
        String id
        String sample
        String library
        String platform
        String platform_unit
        String center
        File r1_fastq
        File r2_fastq
        File reference
        File reference_index
        File reference_dict
        File reference_amb
        File reference_ann
        File reference_bwt
        File reference_sa
        File reference_pac
        Int? num_threads
        String log_dir
        String task_name
        String out_bam
    }

    Int n_threads = if (defined(num_threads)) then select_first([num_threads]) else 64
    Int disk_size = ceil((size(r1_fastq, "GB") + size(r2_fastq, "GB") + size(reference, "GB")) * 2 + 50)

    command {
        set -o pipefail

        ref_dir=$(dirname ~{reference})
        mv ~{reference_index} ~{reference_dict} ~{reference_amb} ~{reference_ann} ~{reference_bwt} ~{reference_sa} ~{reference_pac} $ref_dir

        time \
        bwa mem \
        -t ~{n_threads} \
        -R '@RG\tID:~{id}\tSM:~{sample}\tLB:~{library}\tPL:~{platform}\tPU:~{platform_unit}\tCN:~{center}' \
        ~{reference} \
        ~{r1_fastq} \
        ~{r2_fastq} \
        | sambamba view -S -f bam -o ~{out_bam} /dev/stdin
    }

    output {
        File output_bam = "~{out_bam}"
    }

    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/bwa-sambamba:0.7.8"
        cpu: n_threads
        memory: "57.6 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "48:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task Sort {
    input {
        File input_bam
        Int? num_threads
        String log_dir
        String task_name
        String out_bam
        String out_bai
    }

    Int n_threads = if (defined(num_threads)) then select_first([num_threads]) else 8
    Int disk_size = ceil(size(input_bam, "GB")*2.5 + 50)

    command {
        time \
        sambamba sort \
        -t ~{n_threads} \
        -o ~{out_bam} \
        ~{input_bam}
    }

    output {
        File output_bam = "~{out_bam}"
        File output_bai = "~{out_bai}"
    }

    # n1-standard-8; might need 32 GB memory
    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/bwa-sambamba:0.7.8"
        cpu: n_threads
        memory: "30 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "80:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

# TODO I think this task is using way more resources than it needs
task MergeAligned {
    input {
        Array[File] input_bams
        Array[File] input_bais
        File reference
        File reference_index
        File reference_dict
        String log_dir
        String task_name
        String out_bam
        String out_bai
    }

    Int disk_size = ceil(size(input_bams[0], "GB") * length(input_bams) * 2.5 + 50)

    command {
        set -o pipefail

        ref_dir=$(dirname ~{reference})
        mv ~{reference_index} ~{reference_dict} $ref_dir

        time \
        gatk \
        --java-options "-Xmx20g" \
        MergeSamFiles \
        -O /dev/stdout \
        -I ~{sep=' -I ' input_bams} \
        --ASSUME_SORTED true \
        --SORT_ORDER coordinate \
        | \
        gatk \
        --java-options "-Xmx20g" \
        SetNmMdAndUqTags \
        -I /dev/stdin \
        -O ~{out_bam} \
        -R ~{reference} \
        --CREATE_INDEX true
    }

    output {
        File output_bam = "~{out_bam}"
        File output_bai = "~{out_bai}"
    }

    # n1-standard-16; not sure this much memory is required (cpu fit to memory)
    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/gatk:4.1.3.0"
        cpu: 16
        memory: "56 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "80:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task MarkDuplicates {
    input {
        File input_bam
        File input_bai
        Int? num_threads
        String log_dir
        String task_name
        String out_bam
        String out_bai
    }

    Int n_threads = if (defined(num_threads)) then select_first([num_threads]) else 8
    Int disk_size = ceil(size(input_bam, "GB") * 2 + 20)

    command {
        time \
        sambamba markdup \
        -t ~{n_threads} \
        ~{input_bam} \
        ~{out_bam}
    }

    output {
        File output_bam = "~{out_bam}"
        File output_bai = "~{out_bai}"
    }

    # n1-standard-8
    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/bwa-sambamba:0.7.8"
        cpu: n_threads
        memory: "30 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "48:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task createIntervals {
    input {
        File reference_dict
    }

    command {
        create_sequence_intervals.py \
            --dict ~{reference_dict}
    }

    output {
        Array [Array [String]] sequence_grouping = read_tsv(stdout())
    }

    runtime {
        docker: "gcr.io/cool-benefit-817/dnastack_toolkit:0.0.3"
        cpu: 1
        memory: "3.75 GB"
        disks: "local-disk 20 HDD"
        preemptible: 2
    }
}

# TODO no -nt ~{n_threads} here?
task RealignerTargetCreator {
    input {
        File input_bam
        File input_bai
        File reference
        File reference_index
        File reference_dict
        Array [String] interval_list
        Int n_threads = 4
        String log_dir
        String task_name
        String out_intervals
    }

    Int disk_size = ceil((size(input_bam, "GB") + size(reference, "GB")) * 3 + 100)

    command {
        ref_dir=$(dirname ~{reference})
        mv ~{reference_index} ~{reference_dict} $ref_dir

        time \
        java \
        -Xmx20g \
        -jar "$GATK" \
        -T RealignerTargetCreator \
        -I ~{input_bam} \
        -R ~{reference} \
        -L ~{sep=" -L " interval_list} \
        -l INFO \
        -o ~{out_intervals}
    }

    output {
        File output_intervals = "~{out_intervals}"
    }

    # n1-highmem-4
    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/gatk:3.8"
        cpu: n_threads
        memory: "26 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "100:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task IndelRealigner {
    input {
        File input_bam
        File input_bai
        File reference
        File reference_index
        File reference_dict
        File target_intervals
        Array [String] interval_list
        String log_dir
        String task_name
        String out_bam
        String out_bai
    }

    Int disk_size = ceil((size(input_bam, "GB") + size(reference, "GB")) * 4 + 200)

    command {
        ref_dir=$(dirname ~{reference})
        mv ~{reference_index} ~{reference_dict} $ref_dir

        time \
        java \
        -Xmx24g \
        -jar "$GATK" \
        -T IndelRealigner \
        -I ~{input_bam} \
        -R ~{reference} \
        -L ~{sep=" -L " interval_list} \
        -targetIntervals ~{target_intervals} \
        -o ~{out_bam}
    }

    output {
        File output_bam = "~{out_bam}"
        File output_bai = "~{out_bai}"
    }

    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/gatk:3.8"
        cpu: 4
        memory: "26 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 1
        walltime: "100:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

# TODO no more -nct ~{n_threads} here?
task BaseRecalibrator {
    input {
        File input_bam
        File input_bai
        File reference
        File reference_index
        File reference_dict
        Array [String] interval_list
        File known_sites
        File known_sites_index
        String log_dir
        String task_name
        String out_table
    }

    Int disk_size = ceil((size(input_bam, "GB") + size(reference, "GB")) * 3 + 100)

    command {
        known_sites_dir=$(dirname ~{known_sites})
        mv ~{known_sites_index} $known_sites_dir

        ref_dir=$(dirname ~{reference})
        mv ~{reference_index} ~{reference_dict} $ref_dir

        time \
        gatk \
        --java-options "-Xmx24g" \
        BaseRecalibrator \
        -R ~{reference} \
        -I ~{input_bam} \
        -L ~{sep=" -L " interval_list} \
        -O ~{out_table} \
        --verbosity INFO \
        --known-sites ~{known_sites}
    }

    output {
        File output_table = "~{out_table}"
    }

    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/gatk:4.1.3.0"
        cpu: 8
        memory: "30 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 1
        walltime: "100:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task ApplyBQSR {
    input {
        File input_bam
        File input_bai
        File recal_table
        File reference
        File reference_index
        File reference_dict
        Array [String] interval_list
        String log_dir
        String task_name
        String out_bam
        String out_bai
    }

    Int disk_size = ceil((size(input_bam, "GB") + size(reference, "GB")) * 2 + 100)

    command {
        ref_dir=$(dirname ~{reference})
        mv ~{reference_index} ~{reference_dict} $ref_dir

        time \
        gatk \
        --java-options "-Xmx24g" \
        ApplyBQSR \
        -R ~{reference} \
        -I ~{input_bam} \
        -L ~{sep=" -L " interval_list} \
        -O ~{out_bam} \
        -bqsr ~{recal_table} \
        --verbosity INFO \
        -add-output-sam-program-record
    }

    output {
        File output_bam = "~{out_bam}"
        File output_bai = "~{out_bai}"
    }

    # TODO this is the standin for PrintReads; it's possible it actually requires
    # the 32 GB of memory it was initially set at?
    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/gatk:4.1.3.0"
        cpu: 1
        memory: "3.75 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "100:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

# TODO I think this task is using way more resources than it needs
task MergeRecalibrated {
    input {
        Array[File] input_bams
        Array[File] input_bais
        String log_dir
        String task_name
        String out_bam
        String out_bai
        String out_md5
    }

    Int disk_size = ceil(size(input_bams[0], "GB") * length(input_bams) * 3 + 100)

    command {
        set -o pipefail

        time \
        gatk \
        --java-options "-Xmx20g" \
        MergeSamFiles \
        -O ~{out_bam} \
        -I ~{sep=' -I ' input_bams} \
        --ASSUME_SORTED true \
        --SORT_ORDER coordinate \
        --CREATE_INDEX true \
        --CREATE_MD5_FILE true \
        --USE_THREADING true
    }

    output {
        File output_bam = "~{out_bam}"
        File output_bai = "~{out_bai}"
        File output_md5 = "~{out_md5}"
    }

    # n1-standard-16; not sure this much memory is required (cpu fit to memory)
    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/gatk:4.1.3.0"
        cpu: 16
        memory: "56 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "80:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task Idxstats {
    input {
        File bam
        File bai
        String log_dir
        String task_name
        String out_file
    }

    Int disk_size = ceil(size(bam, "GB") * 1.5 + 20)

    command {
        time \
        samtools idxstats ~{bam} > ~{out_file}
    }

    output {
        File output_file = "~{out_file}"
    }

    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/samtools:1.9"
        cpu: 1
        memory: "3.75 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "48:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task Barchart {
    input {
        File idx_stats
        String sample
        String log_dir
        String task_name
        String out_pdf
    }

    command {
        time \
        Rscript /opt/scripts/barchart.R \
        --input ~{idx_stats} \
        --sample ~{sample} \
        --output ~{out_pdf}
    }

    output {
        File output_pdf = "~{out_pdf}"
    }

    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/shlienlab-libraries:3.4.0"
        cpu: 1
        memory: "3.75 GB"
        disks: "local-disk 20 HDD"
        preemptible: 2
        walltime: "48:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task Flagstat {
    input {
        File bam
        File bai
        String log_dir
        String task_name
        String out_file
    }

    Int disk_size = ceil(size(bam, "GB") * 1.5 + 20)

    command {
        time \
        sambamba flagstat ~{bam} > ~{out_file}
    }

    output {
        File output_file = "~{out_file}"
    }

    # TODO may need more memory/cores (samtools didn't take advantage of it, sambamba m)
    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/bwa-sambamba:0.7.8"
        cpu: 2
        memory: "7.5 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "48:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task DepthOfCoverage {
    input {
        File bam
        File bai
        File reference
        File reference_index
        File reference_dict
        File gatk_intervals
        String log_dir
        String task_name
        String out_prefix
    }

    Int disk_size = ceil((size(bam, "GB") + size(reference, "GB")) * 1.5 + 20)

    command {
        ref_dir=$(dirname ~{reference})
        mv ~{reference_index} ~{reference_dict} $ref_dir

        time \
        java \
        -Xmx8g \
        -jar "$GATK" \
        -T DepthOfCoverage \
        -o ~{out_prefix} \
        -I ~{bam} \
        -R ~{reference} \
        --omitDepthOutputAtEachBase \
        -L ~{gatk_intervals}
    }

    output {
        Array [File] output_files = glob("~{out_prefix}.*")
    }

    # TODO may need more memory; previous iteration used -Xmx4g
    # Runs >24h - no preemptible
    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/gatk:3.8"
        cpu: 2
        memory: "7.5 GB"
        disks: "local-disk " + disk_size + " HDD"
        walltime: "48:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task CollectInsertSizeMetrics {
    input {
        File bam
        File bai
        String log_dir
        String task_name
        String out_metrics
        String out_histogram
    }

    Int disk_size = ceil(size(bam, "GB") * 1.2 + 20)

    command {
        time \
        gatk \
        --java-options "-Xmx8g" \
        CollectInsertSizeMetrics \
        -I ~{bam} \
        -O ~{out_metrics} \
        -H ~{out_histogram} \
        --VALIDATION_STRINGENCY LENIENT
    }

    output {
        File output_metrics = "~{out_metrics}"
        File output_histogram = "~{out_histogram}"
    }

    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/gatk-r:4.1.3.0_3.6.1"
        cpu: 4
        memory: "15 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "4:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}

task FASTQC {
    input {
        File bam
        Int? num_threads
        String log_dir
        String task_name
        String out_zip
        String out_html
    }

    Int n_threads = if (defined(num_threads)) then select_first([num_threads]) else 2
    Int disk_size = ceil(size(bam, "GB") * 1.2 + 20)

    command {
        time \
        fastqc \
        --format bam \
        --outdir . \
        --threads ~{n_threads} \
        ~{bam}
    }

    output {
        File output_zip = "~{out_zip}"
        File output_html = "~{out_html}"
    }

    runtime {
        docker: "gcr.io/cool-benefit-817/private/profyle/fastqc:0.11.8"
        cpu: n_threads
        memory: "7.5 GB"
        disks: "local-disk " + disk_size + " HDD"
        preemptible: 2
        walltime: "48:00:00"
        log_dir: log_dir
        task_name: task_name
    }
}