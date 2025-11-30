import React, { useState, useRef } from "react";
import Button from "../common/Button";
import DropdownMenu from "../common/DropdownMenu";

interface HeaderActionsProps {
  onEdit?: () => void;
  onDelete?: () => void;
  onNewConversation?: () => void;
}

const HeaderActions: React.FC<HeaderActionsProps> = ({
  onEdit,
  onDelete,
  onNewConversation,
}) => {
  const [isOptionsOpen, setIsOptionsOpen] = useState(false);
  const optionsButtonRef = useRef<HTMLButtonElement>(null);

  const dropdownItems = [
    {
      label: "Edit",
      icon: "✏️",
      onClick: () => onEdit?.(),
    },
    {
      label: "Delete",
      icon: "🗑️",
      onClick: () => onDelete?.(),
      variant: "danger" as const,
    },
  ];

  return (
    <div className="flex gap-3">
      <div className="relative">
        <Button
          ref={optionsButtonRef}
          variant="secondary"
          onClick={() => setIsOptionsOpen(!isOptionsOpen)}
        >
          ⚙️ Options
        </Button>
        <DropdownMenu
          isOpen={isOptionsOpen}
          onClose={() => setIsOptionsOpen(false)}
          items={dropdownItems}
          triggerRef={optionsButtonRef as React.RefObject<HTMLElement>}
        />
      </div>
      <Button variant="primary" onClick={onNewConversation}>
        <span>+</span>
        <span>New Conversation</span>
      </Button>
    </div>
  );
};

export default HeaderActions;
