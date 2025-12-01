import React, { useEffect, useRef, useState } from "react";
import { createPortal } from "react-dom";

interface DropdownMenuItem {
  label: string;
  onClick: () => void;
  icon?: string;
  variant?: "default" | "danger";
}

interface DropdownMenuProps {
  isOpen: boolean;
  onClose: () => void;
  items: DropdownMenuItem[];
  triggerRef?: React.RefObject<HTMLElement>;
}

const DropdownMenu: React.FC<DropdownMenuProps> = ({
  isOpen,
  onClose,
  items,
  triggerRef,
}) => {
  const menuRef = useRef<HTMLDivElement>(null);
  const [position, setPosition] = useState({ top: 0, left: 0 });

  useEffect(() => {
    if (!isOpen || !triggerRef?.current) return;

    const updatePosition = () => {
      const triggerRect = triggerRef.current?.getBoundingClientRect();
      if (triggerRect) {
        setPosition({
          top: triggerRect.bottom + 8,
          left: triggerRect.right - 192, // 192px = w-48
        });
      }
    };

    updatePosition();
    window.addEventListener("scroll", updatePosition);
    window.addEventListener("resize", updatePosition);

    return () => {
      window.removeEventListener("scroll", updatePosition);
      window.removeEventListener("resize", updatePosition);
    };
  }, [isOpen, triggerRef]);

  useEffect(() => {
    if (!isOpen) return;

    const handleClickOutside = (event: MouseEvent) => {
      if (
        menuRef.current &&
        !menuRef.current.contains(event.target as Node) &&
        triggerRef?.current &&
        !triggerRef.current.contains(event.target as Node)
      ) {
        onClose();
      }
    };

    const handleEscape = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        onClose();
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    document.addEventListener("keydown", handleEscape);

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
      document.removeEventListener("keydown", handleEscape);
    };
  }, [isOpen, onClose, triggerRef]);

  if (!isOpen) return null;

  return createPortal(
    <div
      ref={menuRef}
      className="fixed w-48 bg-deep-blue/95 backdrop-blur-2xl border border-white/10 rounded-[14px] shadow-[0_8px_32px_rgba(0,0,0,0.4)] overflow-hidden z-100 animate-[fadeInUp_0.2s_ease]"
      style={{
        top: `${position.top}px`,
        left: `${position.left}px`,
      }}
    >
      {items.map((item, index) => (
        <button
          key={index}
          onClick={() => {
            item.onClick();
            onClose();
          }}
          className={`w-full px-4 py-3 text-left flex items-center gap-3 transition-all duration-200 ${
            item.variant === "danger"
              ? "text-orange hover:bg-orange/10"
              : "text-soft-white hover:bg-white/[0.08]"
          } ${index !== items.length - 1 ? "border-b border-white/[0.06]" : ""}`}
        >
          {item.icon && <span className="text-lg">{item.icon}</span>}
          <span className="text-[14px] font-medium">{item.label}</span>
        </button>
      ))}
    </div>,
    document.body
  );
};

export default DropdownMenu;
