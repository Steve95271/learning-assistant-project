import React from "react";
import TopicCard from "./TopicCard";
import type { TopicCardData } from "../../types";

interface TopicsGridProps {
  topics: TopicCardData[];
  onTopicClick: (topic: TopicCardData) => void;
  onTopicMenuClick: (topic: TopicCardData, e: React.MouseEvent) => void;
  newTopicId: string | null;
  newTopicRef: React.RefObject<HTMLDivElement | null>;
}

const TopicsGrid: React.FC<TopicsGridProps> = ({
  topics,
  onTopicClick,
  onTopicMenuClick,
  newTopicId,
  newTopicRef,
}) => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-7 mb-[60px]">
      {topics.map((topic, index) => (
        <TopicCard
          key={topic.id}
          topic={topic}
          index={index}
          onClick={() => onTopicClick(topic)}
          onMenuClick={(e) => onTopicMenuClick(topic, e)}
          ref={topic.id === newTopicId ? newTopicRef : null}
        />
      ))}
    </div>
  );
};

export default TopicsGrid;
