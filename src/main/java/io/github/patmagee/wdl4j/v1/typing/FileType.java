package io.github.patmagee.wdl4j.v1.typing;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
public class FileType implements Type {

    private static final FileType INSTANCE = new FileType();

    private FileType(){

    }

    public static FileType getType() {
        return INSTANCE;
    }

    @Override
    public String getTypeName() {
        return "File";
    }
}
