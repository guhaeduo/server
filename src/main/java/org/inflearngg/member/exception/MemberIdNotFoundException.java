package org.inflearngg.member.exception;

import static org.inflearngg.aop.error.ExceptionErrorMessage.MEMBER_ID_NOT_FOUND;

public class MemberIdNotFoundException extends RuntimeException {
    public MemberIdNotFoundException() {
        super(MEMBER_ID_NOT_FOUND);
    }
}
