export const SUPPORTED_FILE_TYPES = [
  "application/pdf",
  "text/plain",
  "text/markdown",
  "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
  "image/png",
  "image/jpeg",
  "image/heic",
] as const;

export const MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

export interface FileValidationError {
  file: File;
  error: string;
}

export function validateFiles(files: FileList): {
  valid: File[];
  errors: FileValidationError[];
} {
  const valid: File[] = [];
  const errors: FileValidationError[] = [];

  Array.from(files).forEach((file) => {
    if (!SUPPORTED_FILE_TYPES.includes(file.type as any)) {
      errors.push({
        file,
        error: `Unsupported file type: ${file.type}`,
      });
    } else if (file.size > MAX_FILE_SIZE) {
      errors.push({
        file,
        error: `File too large: ${(file.size / 1024 / 1024).toFixed(
          2
        )}MB (max 10MB)`,
      });
    } else {
      valid.push(file);
    }
  });

  return { valid, errors };
}
