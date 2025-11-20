import React from "react";
import type { TopicCardData } from "../../types";

interface TopicCardProps {
  topic: TopicCardData;
  index: number;
  onClick: () => void;
  onMenuClick: (e: React.MouseEvent) => void;
}

const TopicCard: React.FC<TopicCardProps> = ({
  topic,
  index,
  onClick,
  onMenuClick,
}) => {
  const gradientVariants = [
    "before:bg-gradient-to-r before:from-electric before:to-accent-orange",
    "before:bg-gradient-to-r before:from-accent-orange before:to-accent-orange-light",
    "before:bg-gradient-to-r before:from-accent-blue before:to-accent-blue-light",
  ];

  const gradientClass = gradientVariants[index % 3];

  return (
    <div
      onClick={onClick}
      className={`relative overflow-hidden bg-deep-blue/60 backdrop-blur-[40px] border border-white/[0.06] rounded-3xl p-8 cursor-pointer transition-all duration-300 animate-[fadeInUp_0.8s_ease_forwards] opacity-0 hover:-translate-y-2 hover:border-white/[0.12] hover:bg-deep-blue/80 before:content-[''] before:absolute before:top-0 before:left-0 before:w-full before:h-1 ${gradientClass}`}
      style={{ animationDelay: `${0.3 + index * 0.1}s` }}
    >
      <div className="flex justify-between items-start mb-5 gap-4">
        <h3 className="text-[22px] font-semibold tracking-tight">
          {topic.title}
        </h3>
        <button
          onClick={onMenuClick}
          className="w-9 h-9 rounded-[10px] bg-white/[0.04] border border-white/[0.08] flex items-center justify-center cursor-pointer text-lg transition-all duration-300 hover:bg-white/[0.08]"
        >
          ⋮
        </button>
      </div>

      <p className="text-sm text-muted-blue leading-relaxed mb-6 line-clamp-2">
        {topic.description}
      </p>

      <div className="flex gap-6 mb-5 text-[13px] text-muted-blue">
        <div className="flex items-center gap-1.5">
          <span>◷</span>
          <span>Updated {topic.lastUpdated}</span>
        </div>
        <div className="flex items-center gap-1.5">
          <span>📄</span>
          <span>
            {topic.filesCount === 0
              ? "No files"
              : `${topic.filesCount} file${topic.filesCount > 1 ? "s" : ""}`}
          </span>
        </div>
      </div>

      <div className="flex justify-between items-center pt-5 border-t border-white/[0.06]">
        <div className="text-[13px] text-muted-blue font-medium">
          {topic.conversationsCount} conversation
          {topic.conversationsCount !== 1 ? "s" : ""}
        </div>
        {/* TODO File preview is needed the AI pick emoji when uploading file */}
        {/* <div className="flex gap-2">
          {topic.filePreviews.slice(0, 3).map((icon, idx) => (
            <div
              key={idx}
              className="w-8 h-8 rounded-lg bg-white/[0.06] border border-white/[0.08] flex items-center justify-center text-xs"
            >
              {icon}
            </div>
          ))}
          {topic.filePreviews.length > 3 && (
            <div className="w-8 h-8 rounded-lg bg-white/[0.06] border border-white/[0.08] flex items-center justify-center text-[11px] font-semibold text-muted-blue">
              +{topic.filePreviews.length - 3}
            </div>
          )}
        </div> */}
      </div>
    </div>
  );
};

export default TopicCard;
