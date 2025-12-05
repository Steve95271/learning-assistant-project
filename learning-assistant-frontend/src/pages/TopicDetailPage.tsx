import React, { useState } from "react";
import Header from "../components/layout/Header";
import TopicHeader from "../components/topic/TopicHeader";
import ContentGrid from "../components/layout/ContentGrid";
import SessionsSection from "../components/sessions/SessionsSection";
import Sidebar from "../components/layout/Sidebar";
import SummaryCard from "../components/summary/SummaryCard";
import FilesCard from "../components/files/FilesCard";
import EditTopicModal from "../components/topic/EditTopicModal";
import type {
  Topic,
  QuickAction,
  Session,
  SummaryNote,
  FileItem,
  UpdateTopicFormData,
} from "../types";

interface TopicDetailPageProps {
  topic: Topic;
  quickActions: QuickAction[];
  sessions: Session[];
  summary: SummaryNote | null;
  files: FileItem[];
  topicId: string;
  userId: number;
  onBack?: () => void;
  onEdit?: () => void;
  onDelete?: () => void;
  onNewConversation?: () => void;
  onActionClick?: (action: QuickAction) => void;
  onSessionClick?: (session: Session) => void;
  onViewSummary?: () => void;
  onFileClick?: (file: FileItem) => void;
  onFileMenuClick?: (file: FileItem) => void;
  onUploadComplete?: () => void;
  isEditModalOpen?: boolean;
  onEditModalClose?: () => void;
  onEditSubmit?: (data: UpdateTopicFormData) => void;
}

const TopicDetailPage: React.FC<TopicDetailPageProps> = ({
  topic,
  quickActions,
  sessions,
  summary: initialSummary,
  files,
  topicId,
  userId,
  onBack,
  onEdit,
  onDelete,
  onNewConversation,
  onActionClick,
  onSessionClick,
  onViewSummary,
  onFileClick,
  onFileMenuClick,
  onUploadComplete,
  isEditModalOpen = false,
  onEditModalClose,
  onEditSubmit,
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
        onEdit={onEdit}
        onDelete={onDelete}
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
            topicId={topicId}
            userId={userId}
            files={files}
            onFileClick={onFileClick}
            onFileMenuClick={onFileMenuClick}
            onUploadComplete={onUploadComplete}
          />
        </Sidebar>
      </ContentGrid>

      {/* Edit Topic Modal */}
      <EditTopicModal
        isOpen={isEditModalOpen}
        onClose={onEditModalClose || (() => {})}
        onSubmit={onEditSubmit || (() => {})}
        initialData={{
          name: topic.title,
          description: topic.description,
        }}
      />
    </div>
  );
};

export default TopicDetailPage;
