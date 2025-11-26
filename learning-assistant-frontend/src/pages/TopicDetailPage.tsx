import React, { useState } from "react";
import Header from "../components/layout/Header";
import TopicHeader from "../components/topic/TopicHeader";
import ContentGrid from "../components/layout/ContentGrid";
import SessionsSection from "../components/sessions/SessionsSection";
import Sidebar from "../components/layout/Sidebar";
import SummaryCard from "../components/summary/SummaryCard";
import FilesCard from "../components/files/FilesCard";
import type {
  Topic,
  QuickAction,
  Session,
  SummaryNote,
  FileItem,
} from "../types";

interface TopicDetailPageProps {
  topic: Topic;
  quickActions: QuickAction[];
  sessions: Session[];
  summary: SummaryNote | null;
  files: FileItem[];
  onBack?: () => void;
  onSettings?: () => void;
  onNewConversation?: () => void;
  onActionClick?: (action: QuickAction) => void;
  onSessionClick?: (session: Session) => void;
  onViewSummary?: () => void;
  onFileClick?: (file: FileItem) => void;
  onFileMenuClick?: (file: FileItem) => void;
  onUpload?: (files: FileList) => void;
}

const TopicDetailPage: React.FC<TopicDetailPageProps> = ({
  topic,
  quickActions,
  sessions,
  summary: initialSummary,
  files,
  onBack,
  onSettings,
  onNewConversation,
  onActionClick,
  onSessionClick,
  onViewSummary,
  onFileClick,
  onFileMenuClick,
  onUpload,
}) => {
  const [summary, setSummary] = useState(initialSummary);

  const handleToggleAutoUpdate = (checked: boolean) => {
    if (summary) {
      setSummary({ ...summary, autoUpdate: checked });
    }
  };

  return (
    <div className="max-w-[1600px] mx-auto px-[60px] py-10 relative z-10">
      <Header
        onBack={onBack}
        onSettings={onSettings}
        onNewConversation={onNewConversation}
      />
      <TopicHeader topic={topic} />
      <ContentGrid>
        <SessionsSection
          quickActions={quickActions}
          sessions={sessions}
          onActionClick={onActionClick}
          onSessionClick={onSessionClick}
        />
        <Sidebar>
          <SummaryCard
            summary={summary}
            onToggleAutoUpdate={handleToggleAutoUpdate}
            onViewSummary={onViewSummary || (() => {})}
          />
          <FilesCard
            files={files}
            onFileClick={onFileClick}
            onFileMenuClick={onFileMenuClick}
            onUpload={onUpload}
          />
        </Sidebar>
      </ContentGrid>
    </div>
  );
};

export default TopicDetailPage;
