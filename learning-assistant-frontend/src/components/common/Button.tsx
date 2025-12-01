import React, { forwardRef } from "react";

interface ButtonProps {
  children: React.ReactNode;
  variant?: "primary" | "secondary";
  onClick?: () => void;
  className?: string;
}

const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  ({ children, variant = "primary", onClick, className = "" }, ref) => {
    const baseStyles =
      "px-6 py-3 rounded-[14px] text-sm font-semibold cursor-pointer transition-all duration-300 ease-out flex items-center gap-2";

    const variantStyles = {
      primary:
        "bg-gradient-to-br from-electric to-electric-dark text-midnight hover:-translate-y-0.5 hover:shadow-[0_8px_24px_rgba(0,255,136,0.3)]",
      secondary:
        "bg-white/[0.06] border border-white/[0.08] text-soft-white hover:bg-white/10",
    };

    return (
      <button
        ref={ref}
        className={`${baseStyles} ${variantStyles[variant]} ${className}`}
        onClick={onClick}
      >
        {children}
      </button>
    );
  }
);

Button.displayName = "Button";

export default Button;
