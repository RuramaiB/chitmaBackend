package me.ruramaibotso.umc.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LocalsRequest {
    private String localName;
    private String localLocation;
    private String prefix;
}
