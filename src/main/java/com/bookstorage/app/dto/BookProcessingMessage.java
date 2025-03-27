package com.bookstorage.app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookProcessingMessage {

    @JsonProperty("userId")
    private String userId;
    @JsonProperty("dto")
    private BookCreateDTO dto;
    @JsonProperty("fileBytes")
    private byte[] fileBytes;

    public BookProcessingMessage(String userId, BookCreateDTO dto, byte[] fileBytes) {
        this.userId = userId;
        this.dto = dto;
        this.fileBytes = fileBytes;
    }

    public String getUserId() {
        return userId;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }


    public BookCreateDTO getDto() {
        return dto;
    }
}
