package com.nvm.shoestoreapi.dto.goship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WardResponseListWrapper {

    private List<WardResponse> data;

}
