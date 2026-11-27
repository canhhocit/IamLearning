import { useState, type FormEvent } from "react";
import type { User } from "../types";

interface UserTableProps {
  users: User[];
  isLoading: boolean;
  error: string;
  actionNotice: string;
  onUpdateUser: (user: User) => Promise<void>;
  onDeleteUser: (id: string) => Promise<void>;
}

export default function UserTable({
  users,
  isLoading,
  error,
  actionNotice,
  onUpdateUser,
  onDeleteUser,
}: UserTableProps) {
  if (isLoading) {
    return (
      <p className="user-list-message" role="status">
        Loading users…
      </p>
    );
  }

  if (error) {
    return (
      <p className="user-list-message error-message" role="alert">
        {error}
      </p>
    );
  }

  if (users.length === 0) {
    return (
      <>
        {actionNotice && (
          <p className="user-action-notice" role="status">
            {actionNotice}
          </p>
        )}
        <p className="user-list-message">No users yet. Create the first one above.</p>
      </>
    );
  }

  return (
    <>
      {actionNotice && (
        <p className="user-action-notice" role="status">
          {actionNotice}
        </p>
      )}
      <div className="user-table-wrapper">
        <table className="user-table" aria-label="Registered users">
          <thead>
            <tr>
              <th scope="col">STT</th>
              <th scope="col">Name</th>
              <th scope="col">Email</th>
              <th scope="col">Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user, index) => (
              <UserRow
                key={user.id}
                user={user}
                index={index}
                onUpdateUser={onUpdateUser}
                onDeleteUser={onDeleteUser}
              />
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}

interface UserRowProps {
  user: User;
  index: number;
  onUpdateUser: (user: User) => Promise<void>;
  onDeleteUser: (id: string) => Promise<void>;
}

function UserRow({ user, index, onUpdateUser, onDeleteUser }: UserRowProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [isConfirmingDelete, setIsConfirmingDelete] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);
  const [name, setName] = useState(user.name);
  const [email, setEmail] = useState(user.email);
  const [error, setError] = useState("");

  function startEditing() {
    setName(user.name);
    setEmail(user.email);
    setError("");
    setIsConfirmingDelete(false);
    setIsEditing(true);
  }

  async function handleUpdate(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setError("");
    setIsSaving(true);
    try {
      await onUpdateUser({ ...user, name: name.trim(), email: email.trim() });
      setIsEditing(false);
    } catch (requestError) {
      setError(
        requestError instanceof Error
          ? requestError.message
          : "Could not update user. Please try again.",
      );
    } finally {
      setIsSaving(false);
    }
  }

  async function handleDelete() {
    setError("");
    setIsDeleting(true);
    try {
      await onDeleteUser(user.id);
    } catch (requestError) {
      setError(
        requestError instanceof Error
          ? requestError.message
          : "Could not delete user. Please try again.",
      );
    } finally {
      setIsDeleting(false);
    }
  }

  if (isEditing) {
    return (
      <tr>
        <td colSpan={4}>
          <form className="user-edit-form" onSubmit={handleUpdate}>
            <label className="form-field" htmlFor={`user-name-${user.id}`}>
              <span>Name</span>
              <input
                id={`user-name-${user.id}`}
                maxLength={100}
                onChange={(event) => setName(event.target.value)}
                required
                value={name}
              />
            </label>
            <label className="form-field" htmlFor={`user-email-${user.id}`}>
              <span>Email</span>
              <input
                id={`user-email-${user.id}`}
                maxLength={254}
                onChange={(event) => setEmail(event.target.value)}
                required
                type="email"
                value={email}
              />
            </label>
            <div className="user-row-actions">
              <button
                className="user-action-button user-action-button--primary"
                disabled={isSaving}
                type="submit"
              >
                {isSaving ? "Saving..." : "Save changes"}
              </button>
              <button
                className="user-action-button"
                disabled={isSaving}
                onClick={() => setIsEditing(false)}
                type="button"
              >
                Cancel
              </button>
            </div>
            {error && (
              <p className="user-row-error" role="alert">
                {error}
              </p>
            )}
          </form>
        </td>
      </tr>
    );
  }

  return (
    <tr>
      <td>{index + 1}</td>
      <td>{user.name}</td>
      <td>{user.email}</td>
      <td>
        <div className="user-row-actions">
          <button
            className="user-action-button"
            disabled={isDeleting}
            onClick={startEditing}
            type="button"
          >
            Edit
          </button>
          {isConfirmingDelete ? (
            <div
              className="user-delete-confirm"
              role="group"
              aria-label={`Confirm deleting ${user.name}`}
            >
              <span>Delete this user?</span>
              <button
                className="user-action-button"
                disabled={isDeleting}
                onClick={() => setIsConfirmingDelete(false)}
                type="button"
              >
                Cancel
              </button>
              <button
                className="user-action-button user-action-button--danger"
                disabled={isDeleting}
                onClick={handleDelete}
                type="button"
              >
                {isDeleting ? "Deleting..." : "Confirm delete"}
              </button>
            </div>
          ) : (
            <button
              className="user-action-button user-action-button--danger"
              disabled={isDeleting}
              onClick={() => {
                setError("");
                setIsConfirmingDelete(true);
              }}
              type="button"
            >
              Delete
            </button>
          )}
        </div>
        {error && (
          <p className="user-row-error" role="alert">
            {error}
          </p>
        )}
      </td>
    </tr>
  );
}
