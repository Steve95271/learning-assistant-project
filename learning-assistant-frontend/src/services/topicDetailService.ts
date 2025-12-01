import { api } from "./api";
import type { TopicDetailVO, UpdateTopicRequest } from "../types/api";

export const topicDetailService = {
  /**
   * Fetch topic detail by topic ID
   */
  getTopicDetailById: async (topicId: string): Promise<TopicDetailVO> => {
    return api.get<TopicDetailVO>(`/topicDetail/${topicId}`);
  },

  /**
   * Update topic name and description
   */
  updateTopic: async (
    topicId: string,
    data: UpdateTopicRequest
  ): Promise<TopicDetailVO> => {
    return api.patch<TopicDetailVO>(`/topicDetail/${topicId}`, data);
  },

  /**
   * Delete topic by topic ID (soft delete)
   */
  deleteTopic: async (topicId: string): Promise<void> => {
    return api.delete<void>(`/topicDetail/${topicId}`);
  },
};
