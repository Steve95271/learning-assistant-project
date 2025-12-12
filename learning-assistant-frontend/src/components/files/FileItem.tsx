import React, { useRef, useState } from "react";
import type { FileItem as FileItemType } from "../../types";
import DropdownMenu from "../common/DropdownMenu";

interface FileItemProps {
  file: FileItemType;
  onClick?: () => void;
  onDelete?: () => void;
}

const FileItem: React.FC<FileItemProps> = ({ file, onClick, onDelete }) => {
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const menuButtonRef = useRef<HTMLDivElement>(null);

  const menuItems = [
    {
      label: "Delete",
      onClick: () => onDelete?.(),
      icon: "🗑️",
      variant: "danger" as const,
    },
  ];

  return (
    <div
      onClick={onClick}
      className="group flex items-center gap-3.5 p-4 bg-white/[0.03] border border-white/[0.06] rounded-[14px] cursor-pointer transition-all duration-300 hover:bg-white/[0.05] hover:translate-x-1"
    >
      <div className="w-10 h-10 rounded-[10px] bg-electric/10 flex items-center justify-center text-lg flex-shrink-0">
        {file.icon}
      </div>
      <div className="flex-1 min-w-0">
        <div className="text-sm font-semibold mb-1 whitespace-nowrap overflow-hidden text-ellipsis">
          {file.name}
        </div>
        <div className="text-xs text-muted-blue">{file.size}</div>
      </div>
      <div
        ref={menuButtonRef}
        onClick={(e) => {
          e.stopPropagation();
          setIsMenuOpen(!isMenuOpen);
        }}
        className="w-8 h-8 rounded-lg bg-white/[0.04] flex items-center justify-center opacity-0 transition-all duration-300 group-hover:opacity-100"
      >
        ⋮
      </div>

      {/* Dropdown menu */}
      <DropdownMenu
        isOpen={isMenuOpen}
        onClose={() => setIsMenuOpen(false)}
        items={menuItems}
        triggerRef={menuButtonRef as React.RefObject<HTMLElement>}
      />
    </div>
  );
};

export default FileItem;
