import type {
  TopicDetailVO,
  SessionSummaryDTO,
  FileInfoPreviewDTO,
} from "../types/api";
import type {
  Topic,
  Session,
  SummaryNote,
  FileItem,
  QuickAction,
} from "../types";

/**
 * Transform backend TopicDetailVO to frontend types
 */

export function transformToTopic(data: TopicDetailVO, topicId: string): Topic {
  return {
    id: topicId,
    icon: "📚", // TODO fetch icon from backend when AI generate icon feature is completed
    title: data.topicTitle,
    description: data.topicDescription,
    stats: {
      conversations: data.conversations,
      files: data.numberOfFiles,
      lastUpdated: data.lastUpdated,
    },
  };
}

export function transformToSessions(
  sessionSummaries: SessionSummaryDTO[]
): Session[] {
  return sessionSummaries.map((session) => ({
    id: String(session.id),
    icon: "💬", // Default icon for sessions
    title: session.title,
    preview: session.preview,
    meta: {
      timestamp: session.lastChat,
      messageCount: session.messageCount,
    },
  }));
}

export function transformToSummaryNote(
  preview: TopicDetailVO["summaryNotePreview"]
): SummaryNote | null {
  if (!preview) {
    return null;
  }

  return {
    title: preview.summaryNoteTitle,
    wordCount: preview.wordCount,
    conversationCount: preview.numberOfConversations,
    lastUpdated: preview.lastUpdated,
    autoUpdate: preview.autoUpdateEnabled,
  };
}

export function transformToFiles(
  fileInfoPreviews: FileInfoPreviewDTO[]
): FileItem[] {
  return fileInfoPreviews.map((file) => ({
    id: String(file.fileId),
    name: file.fileName,
    size: formatFileSize(file.size),
    icon: file.icon,
  }));
}

/**
 * Format file size from bytes to human-readable format
 */
function formatFileSize(bytes: number): string {
  if (bytes === 0) return "0 B";

  const k = 1024;
  const sizes = ["B", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));

  return `${parseFloat((bytes / Math.pow(k, i)).toFixed(1))} ${sizes[i]}`;
}

/**
 * Generate quick actions based on topic state
 * This could be customized based on backend data in the future
 */
export function generateQuickActions(): QuickAction[] {
  return [
    {
      icon: "📝",
      label: "Review Summary",
      hint: "View your complete learning summary",
      gradient: "primary" as const,
    },
    {
      icon: "📎",
      label: "Upload File",
      hint: "Add resources to this topic",
      gradient: "orange" as const,
    },
    {
      icon: "💾",
      label: "Export Notes",
      hint: "Download your notes",
      gradient: "blue" as const,
    },
  ];
}
