package com.example.checkscam.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReasonsJsonDto {
    private List<Reason> reasons;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Reason{
        private String name;
        private Integer quantity;

        public boolean isExitByName(String name) {
            return this.name.equals(name);
        }
    }
}
