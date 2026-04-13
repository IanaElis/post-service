package com.iana.postservice.dtos.post;

public record ModerationDecisionDto (
    Integer postId,
    boolean approved
){}
