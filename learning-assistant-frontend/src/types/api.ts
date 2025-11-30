/**
 * Backend API response types
 */

// Topics Library API types
export interface TopicLibraryViewDTO {
  id: string;
  title: string;
  description: string;
  lastUpdated: string;
  filesCount: number;
  conversationsCount: number;
  filePreviews: string[]; // emoji icons
}

export interface CreateTopicRequest {
  name: string;
  description?: string;
}

export interface UpdateTopicRequest {
  name: string;
  description?: string;
}

// Topic Detail API types
export interface SessionSummaryDTO {
  id: number;
  title: string;
  preview: string;
  lastChat: string;
  messageCount: number;
}

export interface SummaryNotePreviewDTO {
  summaryNoteTitle: string;
  numberOfConversations: number;
  autoUpdateEnabled: boolean;
  lastUpdated: string;
  wordCount: number;
}

export interface FileInfoPreviewDTO {
  fileId: number;
  icon: string;
  fileName: string;
  size: number;
}

export interface TopicDetailVO {
  topicTitle: string;
  topicDescription: string;
  conversations: number;
  numberOfFiles: number;
  lastUpdated: string;
  sessionSummaries: SessionSummaryDTO[];
  summaryNotePreview: SummaryNotePreviewDTO | null;
  fileInfoPreviews: FileInfoPreviewDTO[];
}
