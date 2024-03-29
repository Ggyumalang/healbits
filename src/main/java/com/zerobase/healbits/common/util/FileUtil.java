package com.zerobase.healbits.common.util;

import com.zerobase.healbits.common.exception.HealBitsException;
import com.zerobase.healbits.common.type.ErrorCode;
import com.zerobase.healbits.challenge.type.ChallengeCategory;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.UUID;

@UtilityClass
public class FileUtil {
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String DIRECTORY_SEPARATOR = "/";
    private static final String FILE_NAME_SEPARATOR = "_";
    private static final String REPLACE_EXPRESSION = "-";

    public static String createFileName(String fileName) {
        return UUID.randomUUID().toString().replaceAll(REPLACE_EXPRESSION, "")
                .concat(FILE_NAME_SEPARATOR)
                .concat(String.valueOf(LocalDate.now()).replaceAll(REPLACE_EXPRESSION, ""))
                .concat(getFileExtension(fileName));
    }

    public static String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf(FILE_EXTENSION_SEPARATOR));
        } catch (Exception e) {
            throw new HealBitsException(ErrorCode.INVALID_FILE_FORMAT);
        }
    }

    public static String createFilePath(
            ChallengeCategory challengeCategory
            , String challengeName
            , String username
    ) {
        return challengeCategory.name() + DIRECTORY_SEPARATOR
                + challengeName + DIRECTORY_SEPARATOR
                + username + DIRECTORY_SEPARATOR;
    }
}
