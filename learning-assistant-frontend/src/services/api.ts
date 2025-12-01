/**
 * API service using Fetch API
 */

const API_BASE_URL = "/api/v1";

class ApiError extends Error {
  public status: number;
  public statusText: string;

  constructor(
    status: number,
    statusText: string,
    message?: string
  ) {
    super(message || `API Error: ${status} ${statusText}`);
    this.name = "ApiError";
    this.status = status;
    this.statusText = statusText;
  }
}

async function fetchJSON<T>(url: string, options?: RequestInit): Promise<T> {
  try {
    const response = await fetch(`${API_BASE_URL}${url}`, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...options?.headers,
      },
    });

    if (!response.ok) {
      throw new ApiError(response.status, response.statusText);
    }

    // Check if response has content before parsing JSON
    // 204 No Content responses have no body
    if (response.status === 204 || response.headers.get('content-length') === '0') {
      return undefined as T;
    }

    return await response.json();
  } catch (error) {
    if (error instanceof ApiError) {
      throw error;
    }
    throw new Error(
      `Network error: ${
        error instanceof Error ? error.message : "Unknown error"
      }`
    );
  }
}

export const api = {
  get: <T>(url: string, options?: RequestInit) =>
    fetchJSON<T>(url, { ...options, method: "GET" }),
  post: <T>(url: string, data?: unknown, options?: RequestInit) =>
    fetchJSON<T>(url, {
      ...options,
      method: "POST",
      body: JSON.stringify(data),
    }),
  put: <T>(url: string, data?: unknown, options?: RequestInit) =>
    fetchJSON<T>(url, {
      ...options,
      method: "PUT",
      body: JSON.stringify(data),
    }),
  patch: <T>(url: string, data?: unknown, options?: RequestInit) =>
    fetchJSON<T>(url, {
      ...options,
      method: "PATCH",
      body: JSON.stringify(data),
    }),
  delete: <T>(url: string, options?: RequestInit) =>
    fetchJSON<T>(url, { ...options, method: "DELETE" }),
};

export { ApiError };
