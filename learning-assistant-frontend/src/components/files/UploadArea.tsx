import React, { useState } from "react";
import { toast } from "sonner";
import { fileUploadService } from "../../services/fileUploadService";
import { validateFiles } from "../../utils/fileValidation";

interface UploadAreaProps {
  topicId: string;
  userId: number;
  onUploadComplete?: () => void;
}

interface UploadingFile {
  file: File;
  fileId?: number;
  progress: number;
  status: "uploading" | "success" | "error";
  error?: string;
}

const UploadArea: React.FC<UploadAreaProps> = ({
  topicId,
  userId,
  onUploadComplete,
}) => {
  const [uploadingFiles, setUploadingFiles] = useState<UploadingFile[]>([]);
  const [isDragging, setIsDragging] = useState(false);

  const handleFiles = async (files: FileList) => {
    // Validate files
    const { valid, errors } = validateFiles(files);

    // Show validation errors
    errors.forEach(({ file, error }) => {
      toast.error(`${file.name}: ${error}`);
    });

    // Upload each valid file
    for (const file of valid) {
      await uploadFile(file);
    }
  };

  const uploadFile = async (file: File) => {
    // Add to uploading list
    const uploadingFile: UploadingFile = {
      file,
      progress: 0,
      status: "uploading",
    };

    setUploadingFiles((prev) => [...prev, uploadingFile]);

    let uploadResponse;

    try {
      // Step 1: Request pre-signed URL
      uploadResponse = await fileUploadService.initiateUpload({
        topicId: Number(topicId),
        userId,
        filename: file.name,
        fileType: file.type,
        fileSize: file.size,
      });

      // Update with fileId
      setUploadingFiles((prev) =>
        prev.map((f) =>
          f.file === file ? { ...f, fileId: uploadResponse!.fileId } : f
        )
      );

      // Step 2: Upload to S3 with progress tracking
      await fileUploadService.uploadToS3(
        file,
        uploadResponse.presignedUrl,
        (progress) => {
          setUploadingFiles((prev) =>
            prev.map((f) => (f.file === file ? { ...f, progress } : f))
          );
        }
      );

      // Step 3: Confirm successful upload
      await fileUploadService.confirmUpload({
        fileId: uploadResponse.fileId,
        success: true,
      });

      // Update status to success
      setUploadingFiles((prev) =>
        prev.map((f) =>
          f.file === file ? { ...f, status: "success", progress: 100 } : f
        )
      );

      toast.success(`${file.name} uploaded successfully`);

      // Notify parent to refresh file list
      onUploadComplete?.();

      // Remove from uploading list after a delay
      setTimeout(() => {
        setUploadingFiles((prev) => prev.filter((f) => f.file !== file));
      }, 2000);
    } catch (error) {
      // Step 4: Handle errors - notify backend of failure
      if (uploadResponse?.fileId) {
        try {
          await fileUploadService.confirmUpload({
            fileId: uploadResponse.fileId,
            success: false,
            errorMessage:
              error instanceof Error ? error.message : "Unknown error",
          });
        } catch (confirmError) {
          console.error("Failed to confirm upload failure:", confirmError);
        }
      }

      // Update status to error
      const errorMessage =
        error instanceof Error ? error.message : "Upload failed";
      setUploadingFiles((prev) =>
        prev.map((f) =>
          f.file === file ? { ...f, status: "error", error: errorMessage } : f
        )
      );

      toast.error(`Failed to upload ${file.name}: ${errorMessage}`);

      // Remove from uploading list after a delay
      setTimeout(() => {
        setUploadingFiles((prev) => prev.filter((f) => f.file !== file));
      }, 5000);
    }
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      handleFiles(e.target.files);
      e.target.value = ""; // Reset input
    }
  };

  const handleDrop = (e: React.DragEvent<HTMLLabelElement>) => {
    e.preventDefault();
    setIsDragging(false);

    if (e.dataTransfer.files && e.dataTransfer.files.length > 0) {
      handleFiles(e.dataTransfer.files);
    }
  };

  const handleDragOver = (e: React.DragEvent<HTMLLabelElement>) => {
    e.preventDefault();
    setIsDragging(true);
  };

  const handleDragLeave = (e: React.DragEvent<HTMLLabelElement>) => {
    e.preventDefault();
    setIsDragging(false);
  };

  return (
    <div className="mt-4">
      <label
        className={`block p-8 border-2 border-dashed rounded-[14px] text-center cursor-pointer transition-all duration-300 ${
          isDragging
            ? "border-electric bg-electric/[0.08]"
            : "border-white/10 hover:border-electric hover:bg-electric/[0.03]"
        }`}
        onDrop={handleDrop}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
      >
        <input
          type="file"
          multiple
          accept=".pdf,.txt,.md,.docx,.png,.jpg,.jpeg,.heic"
          className="hidden"
          onChange={handleFileChange}
        />
        <div className="text-[32px] mb-3 opacity-50">📎</div>
        <div className="text-sm text-muted-blue">
          {isDragging ? "Drop files here" : "Drop files or click to upload"}
        </div>
        <div className="text-xs text-muted-blue/60 mt-2">
          PDF, TXT, MD, DOCX, PNG, JPG, HEIC (max 10MB)
        </div>
      </label>

      {/* Upload progress indicators */}
      {uploadingFiles.length > 0 && (
        <div className="mt-4 space-y-2">
          {uploadingFiles.map((uploadingFile, index) => (
            <div
              key={index}
              className="p-3 bg-deep-blue/40 rounded-lg border border-white/[0.06]"
            >
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm truncate flex-1 mr-2">
                  {uploadingFile.file.name}
                </span>
                <span className="text-xs text-muted-blue">
                  {uploadingFile.status === "uploading" &&
                    `${Math.round(uploadingFile.progress)}%`}
                  {uploadingFile.status === "success" && "✓"}
                  {uploadingFile.status === "error" && "✗"}
                </span>
              </div>
              {uploadingFile.status === "uploading" && (
                <div className="w-full bg-white/5 rounded-full h-1.5">
                  <div
                    className="bg-electric h-1.5 rounded-full transition-all duration-300"
                    style={{ width: `${uploadingFile.progress}%` }}
                  />
                </div>
              )}
              {uploadingFile.status === "error" && uploadingFile.error && (
                <div className="text-xs text-red-400 mt-1">
                  {uploadingFile.error}
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default UploadArea;
