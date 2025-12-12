import React from "react";
import FileItem from "./FileItem";
import UploadArea from "./UploadArea";
import type { FileItem as FileItemType } from "../../types";

interface FilesCardProps {
  topicId: string;
  userId: number;
  files: FileItemType[];
  onFileClick?: (file: FileItemType) => void;
  onFileDelete?: (file: FileItemType) => void;
  onUploadComplete?: () => void;
}

const FilesCard: React.FC<FilesCardProps> = ({
  topicId,
  userId,
  files,
  onFileClick,
  onFileDelete,
  onUploadComplete,
}) => {
  return (
    <div className="bg-deep-blue/60 backdrop-blur-[40px] border border-white/[0.06] rounded-[20px] p-7">
      <h3 className="text-lg font-semibold mb-5 tracking-tight">Files</h3>
      <div className="flex flex-col gap-3">
        {files.map((file) => (
          <FileItem
            key={file.id}
            file={file}
            onClick={() => onFileClick?.(file)}
            onDelete={() => onFileDelete?.(file)}
          />
        ))}
      </div>
      <UploadArea
        topicId={topicId}
        userId={userId}
        onUploadComplete={onUploadComplete}
      />
    </div>
  );
};

export default FilesCard;
