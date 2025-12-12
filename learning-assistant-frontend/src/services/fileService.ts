import { api } from "./api";
import type {
  FileUploadRequest,
  FileUploadResponse,
  ConfirmUploadRequest,
} from "../types/api";

export const fileService = {
  /**
   * Initiates a file upload by requesting a pre-signed S3 URL from the backend.
   */
  initiateUpload: async (
    request: FileUploadRequest
  ): Promise<FileUploadResponse> => {
    return api.post<FileUploadResponse>("/files/initiate-upload", request);
  },

  /**
   * Confirms the upload result (success or failure) to the backend.
   */
  confirmUpload: async (confirmation: ConfirmUploadRequest): Promise<void> => {
    return api.post<void>("/files/confirm-upload", confirmation);
  },

  /**
   * Uploads a file to S3 using the pre-signed URL.
   * Supports progress tracking via optional callback.
   */
  uploadToS3: async (
    file: File,
    presignedUrl: string,
    onProgress?: (progress: number) => void
  ): Promise<void> => {
    return new Promise((resolve, reject) => {
      const xhr = new XMLHttpRequest();

      xhr.upload.addEventListener("progress", (e) => {
        if (e.lengthComputable) {
          const progress = (e.loaded / e.total) * 100;
          onProgress?.(progress);
        }
      });

      xhr.addEventListener("load", () => {
        if (xhr.status >= 200 && xhr.status < 300) {
          resolve();
        } else {
          reject(new Error(`Upload failed with status ${xhr.status}`));
        }
      });

      xhr.addEventListener("error", () => {
        reject(new Error("Network error during upload"));
      });

      xhr.addEventListener("abort", () => {
        reject(new Error("Upload cancelled"));
      });

      xhr.open("PUT", presignedUrl);
      xhr.setRequestHeader("Content-Type", file.type);
      xhr.send(file);
    });
  },

  /**
   * Delete a file from the topic.
   * Performs soft delete in database and hard delete in S3.
   * @param fileId
   * @returns 204 No Content if success
   */
  deleteFile: async (fileId: string): Promise<void> => {
    return api.delete<void>(`/files/${fileId}`);
  },
};
