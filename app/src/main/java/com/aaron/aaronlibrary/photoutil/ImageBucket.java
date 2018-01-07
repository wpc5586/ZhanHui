package com.aaron.aaronlibrary.photoutil;

import java.io.Serializable;
import java.util.List;

public class ImageBucket implements Serializable {
	/**
     * 
     */
    private static final long serialVersionUID = 8006560639067718483L;
    public int count = 0;
	public String bucketName;
	public List<ImageItem> imageList;
}
