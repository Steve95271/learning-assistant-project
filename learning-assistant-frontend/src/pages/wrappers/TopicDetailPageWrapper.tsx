import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "sonner";
import TopicDetailPage from "../TopicDetailPage";
import { topicDetailService } from "../../services/topicDetailService";
import {
  transformToTopic,
  transformToSessions,
  transformToSummaryNote,
  transformToFiles,
  generateQuickActions,
} from "../../utils/transformers";
import type {
  Topic,
  QuickAction,
  Session,
  SummaryNote,
  FileItem,
  UpdateTopicFormData,
} from "../../types";
import type { TopicDetailVO } from "../../types/api";

function TopicDetailPageWrapper() {
  const navigate = useNavigate();
  const { topicId } = useParams<{ topicId: string }>();

  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [topic, setTopic] = useState<Topic | null>(null);
  const [quickActions, setQuickActions] = useState<QuickAction[]>([]);
  const [sessions, setSessions] = useState<Session[]>([]);
  const [summary, setSummary] = useState<SummaryNote | null>(null);
  const [files, setFiles] = useState<FileItem[]>([]);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);

  useEffect(() => {
    const fetchTopicDetail = async () => {
      if (!topicId) {
        setError("Topic ID is missing");
        setIsLoading(false);
        return;
      }

      try {
        setIsLoading(true);
        setError(null);

        const data: TopicDetailVO = await topicDetailService.getTopicDetailById(
          topicId
        );

        // Transform backend data to frontend types
        setTopic(transformToTopic(data, topicId));
        setQuickActions(generateQuickActions());
        setSessions(transformToSessions(data.sessionSummaries));
        setSummary(transformToSummaryNote(data.summaryNotePreview));
        setFiles(transformToFiles(data.fileInfoPreviews));
      } catch (err) {
        const errorMessage =
          err instanceof Error ? err.message : "Failed to load topic details";
        setError(errorMessage);
        toast.error(errorMessage);
        console.error("Error fetching topic detail:", err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchTopicDetail();
  }, [topicId]);

  const handleBack = () => {
    navigate("/");
  };

  const handleEdit = () => {
    setIsEditModalOpen(true);
  };

  const handleDelete = async () => {
    if (!topicId) return;

    // Confirm deletion with user
    const confirmed = window.confirm(
      `Are you sure you want to delete "${topic?.title}"? This action cannot be undone.`
    );

    if (!confirmed) return;

    try {
      await topicDetailService.deleteTopic(topicId);
      toast.success("Topic deleted successfully!");
      navigate("/");
    } catch (err) {
      const errorMessage =
        err instanceof Error ? err.message : "Failed to delete topic";
      toast.error(errorMessage);
      console.error("Error deleting topic:", err);
    }
  };

  const handleEditSubmit = async (data: UpdateTopicFormData) => {
    if (!topicId) return;

    try {
      const updatedData: TopicDetailVO = await topicDetailService.updateTopic(
        topicId,
        data
      );

      // Update local state with new data
      setTopic(transformToTopic(updatedData, topicId));
      setIsEditModalOpen(false);
      toast.success("Topic updated successfully!");
    } catch (err) {
      const errorMessage =
        err instanceof Error ? err.message : "Failed to update topic";
      toast.error(errorMessage);
      console.error("Error updating topic:", err);
    }
  };

  const handleNewConversation = () => {
    if (!topicId) return;
    console.log("Create new conversation");
    navigate(`/topic/${topicId}/session/new`);
  };

  const handleActionClick = (action: QuickAction) => {
    console.log("Action clicked:", action.label);

    switch (action.label) {
      case "Review Summary":
        handleViewSummary();
        break;
      case "Upload File":
        toast.info("Upload file feature coming soon");
        break;
      case "Export Notes":
        toast.info("Export notes feature coming soon");
        break;
      default:
        console.log("Unknown action:", action.label);
    }
  };

  const handleSessionClick = (session: Session) => {
    if (!topicId) return;
    console.log("Session clicked:", session.title);
    navigate(`/topic/${topicId}/session/${session.id}`);
  };

  const handleViewSummary = () => {
    console.log("View full summary");
    toast.info("Full summary view coming soon");
  };

  const handleFileClick = (file: FileItem) => {
    console.log("File clicked:", file.name);
    toast.info("File preview coming soon");
  };

  const handleFileMenuClick = (file: FileItem) => {
    console.log("File menu clicked:", file.name);
  };

  const handleUpload = (files: FileList) => {
    console.log(
      "Files uploaded:",
      Array.from(files).map((f) => f.name)
    );
    toast.success(`${files.length} file(s) uploaded successfully`);
  };

  // Loading state
  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center">
          <div className="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600 mb-4"></div>
          <p className="text-gray-400">Loading topic details...</p>
        </div>
      </div>
    );
  }

  // Error state
  if (error || !topic) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <div className="text-center max-w-md">
          <div className="text-red-500 text-5xl mb-4">⚠️</div>
          <h2 className="text-xl font-semibold text-white mb-2">
            Failed to Load Topic
          </h2>
          <p className="text-gray-400 mb-6">
            {error || "Topic data is incomplete"}
          </p>
          <button
            onClick={handleBack}
            className="px-6 py-2 bg-purple-600 hover:bg-purple-700 text-white rounded-lg transition-colors"
          >
            Back to Topics
          </button>
        </div>
      </div>
    );
  }

  return (
    <TopicDetailPage
      topic={topic}
      quickActions={quickActions}
      sessions={sessions}
      summary={summary}
      files={files}
      onBack={handleBack}
      onEdit={handleEdit}
      onDelete={handleDelete}
      onNewConversation={handleNewConversation}
      onActionClick={handleActionClick}
      onSessionClick={handleSessionClick}
      onViewSummary={handleViewSummary}
      onFileClick={handleFileClick}
      onFileMenuClick={handleFileMenuClick}
      onUpload={handleUpload}
      isEditModalOpen={isEditModalOpen}
      onEditModalClose={() => setIsEditModalOpen(false)}
      onEditSubmit={handleEditSubmit}
    />
  );
}

export default TopicDetailPageWrapper;
