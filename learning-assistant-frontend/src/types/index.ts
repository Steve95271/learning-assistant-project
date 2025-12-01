export interface TopicStats {
  conversations: number;
  files: number;
  lastUpdated: string;
}

export interface Topic {
  id: string;
  icon: string;
  title: string;
  description: string;
  stats: TopicStats;
}

export interface SessionMeta {
  timestamp: string;
  messageCount: number;
}

export interface Session {
  id: string;
  icon: string;
  title: string;
  preview: string;
  meta: SessionMeta;
}

export interface FileItem {
  id: string;
  name: string;
  size: string;
  icon: string;
}

export interface SummaryNote {
  title: string;
  conversationCount: number;
  wordCount: number;
  lastUpdated: string;
  autoUpdate: boolean;
}

export interface QuickAction {
  icon: string;
  label: string;
  hint: string;
  gradient: 'primary' | 'orange' | 'blue';
}

export interface StatItemProps {
  icon: string;
  label: string;
}

// Topics Library Page Types
export interface TopicCardData {
  id: string;
  title: string;
  description: string;
  lastUpdated: string;
  filesCount: number;
  conversationsCount: number;
  filePreviews: string[]; // emoji icons
}

export interface NewTopicFormData {
  name: string;
  description?: string;
}

export interface UpdateTopicFormData {
  name: string;
  description?: string;
}

// Chat Session Page Types
export interface Message {
  id: string;
  role: 'user' | 'ai';
  content: string;
  timestamp?: string;
  fileReferences?: string[];
}

export interface ChatSession {
  id: string;
  topicId: string;
  topicTitle: string;
  topicIcon: string;
  messages: Message[];
}

export interface AttachedFile {
  id: string;
  name: string;
  icon: string;
}

// Navigation Types
export interface NavLink {
  label: string;
  href: string;
  active?: boolean;
}
