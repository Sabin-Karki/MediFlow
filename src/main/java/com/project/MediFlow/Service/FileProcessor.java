package com.project.MediFlow.Service;

import com.project.MediFlow.Model.RawDataEvent;

public interface FileProcessor {
    void process(RawDataEvent rawDataEvent);
    String getFileType();
}
