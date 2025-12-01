import React, { useState, useEffect } from "react";
import Modal from "../common/Modal";
import type { UpdateTopicFormData } from "../../types";

interface EditTopicModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSubmit: (data: UpdateTopicFormData) => void;
  initialData: {
    name: string;
    description: string;
  };
}

const EditTopicModal: React.FC<EditTopicModalProps> = ({
  isOpen,
  onClose,
  onSubmit,
  initialData,
}) => {
  const [formData, setFormData] = useState<UpdateTopicFormData>({
    name: initialData.name,
    description: initialData.description,
  });

  // Reset form when modal opens with new initial data
  useEffect(() => {
    if (isOpen) {
      setFormData({
        name: initialData.name,
        description: initialData.description,
      });
    }
  }, [isOpen, initialData]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const handleCancel = () => {
    // Reset to initial data when canceling
    setFormData({
      name: initialData.name,
      description: initialData.description,
    });
    onClose();
  };

  return (
    <Modal isOpen={isOpen} onClose={handleCancel} title="Edit Topic">
      <form onSubmit={handleSubmit}>
        {/* Name Input */}
        <div className="mb-6">
          <label
            htmlFor="topicName"
            className="block text-[13px] font-semibold text-warm-gray mb-2.5 tracking-[0.5px] uppercase"
          >
            Topic Name
          </label>
          <input
            type="text"
            id="topicName"
            value={formData.name}
            onChange={(e) => setFormData({ ...formData, name: e.target.value })}
            placeholder="e.g., Machine Learning Basics"
            required
            className="w-full px-5 py-4 bg-white/[0.04] border border-white/[0.08] rounded-[14px] text-soft-white text-[15px] transition-all duration-300 focus:outline-none focus:border-electric focus:bg-white/[0.06] focus:shadow-[0_0_0_4px_rgba(0,255,136,0.1)] placeholder:text-muted-blue"
          />
        </div>

        {/* Description Input */}
        <div className="mb-8">
          <label
            htmlFor="topicDescription"
            className="block text-[13px] font-semibold text-warm-gray mb-2.5 tracking-[0.5px] uppercase"
          >
            Description
          </label>
          <textarea
            id="topicDescription"
            value={formData.description}
            onChange={(e) =>
              setFormData({ ...formData, description: e.target.value })
            }
            placeholder="What will you learn in this topic?"
            rows={4}
            className="w-full px-5 py-4 bg-white/[0.04] border border-white/[0.08] rounded-[14px] text-soft-white text-[15px] transition-all duration-300 resize-y min-h-[100px] focus:outline-none focus:border-electric focus:bg-white/[0.06] focus:shadow-[0_0_0_4px_rgba(0,255,136,0.1)] placeholder:text-muted-blue"
          />
        </div>

        {/* Action Buttons */}
        <div className="flex gap-3">
          <button
            type="button"
            onClick={handleCancel}
            className="flex-1 px-4 py-4 rounded-[14px] text-base font-semibold tracking-[0.3px] bg-white/[0.06] border border-white/[0.08] text-soft-white transition-all duration-300 hover:bg-white/10"
          >
            Cancel
          </button>
          <button
            type="submit"
            className="flex-1 px-4 py-4 rounded-[14px] text-base font-semibold tracking-[0.3px] bg-gradient-to-br from-electric to-electric-dark text-midnight transition-all duration-300 hover:-translate-y-0.5 hover:shadow-[0_8px_24px_rgba(0,255,136,0.3)]"
          >
            Save Changes
          </button>
        </div>
      </form>
    </Modal>
  );
};

export default EditTopicModal;
