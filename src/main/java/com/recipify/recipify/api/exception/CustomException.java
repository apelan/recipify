package com.recipify.recipify.api.exception;

import java.sql.Timestamp;
import java.util.Set;

public record CustomException(int status, Set<String> messages, Timestamp timestamp) {
}
