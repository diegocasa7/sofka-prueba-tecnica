const BASE_URL = "http://localhost:8080";

export type ApiError = {
  timestamp?: string;
  status?: number;
  error?: string;
  message?: string;
  path?: string;
};

export async function apiFetch<T>(
  path: string,
  options: RequestInit = {}
): Promise<T> {
  const res = await fetch(`${BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  if (!res.ok) {
    let err: ApiError | undefined;
    try {
      err = await res.json();
    } catch {
      // ignore
    }
    throw new Error(err?.message || `Error HTTP ${res.status}`);
  }

  return (await res.json()) as T;
}
