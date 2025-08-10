package com.payup.payupapp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupRequest {
    String groupName;
    String createdBy;
    List<String> members;
}
