version 1.0

task only_output {
    command {}

    output {
        String a = ""
    }
}

task only_input {
    input {
        String a
    }
    command {}
}

task only_runtime {
    runtime {
        docker: "test"
    }

    command {}
}