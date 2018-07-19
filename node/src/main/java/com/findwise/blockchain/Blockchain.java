package com.findwise.blockchain;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.LinkedList;

@JsonRootName("blockchain")
@Data
public class Blockchain {
    private LinkedList<Block> list;
}
