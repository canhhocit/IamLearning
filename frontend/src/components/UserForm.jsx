import { useState } from "react";
import { userApi } from "../api/userApi.js";

export default function UserForm() {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [createdUser, setCreatedUser] = useState(null);

  async function handleSubmit(event) {
    event.preventDefault();
    setError("");
    setCreatedUser(null);
    setIsSubmitting(true);

    try {
      const user = await userApi.create(name.trim(), email.trim());
      setCreatedUser(user);
      setName("");
      setEmail("");
    } catch (requestError) {
      setError(requestError.message || "Could not create user. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  }

  function clearFeedback() {
    setError("");
    setCreatedUser(null);
  }

  return (
    <form className="user-form" onSubmit={handleSubmit}>
      <label className="form-field" htmlFor="user-name">
        <span>Name</span>
        <input
          autoComplete="name"
          id="user-name"
          maxLength={100}
          onChange={(event) => {
            setName(event.target.value);
            clearFeedback();
          }}
          placeholder="e.g. Alex Nguyen"
          required
          value={name}
        />
      </label>

      <label className="form-field" htmlFor="user-email">
        <span>Email</span>
        <input
          autoComplete="email"
          id="user-email"
          maxLength={254}
          onChange={(event) => {
            setEmail(event.target.value);
            clearFeedback();
          }}
          placeholder="alex@example.com"
          required
          type="email"
          value={email}
        />
      </label>

      <button className="user-submit" disabled={isSubmitting} type="submit">
        {isSubmitting ? "Creating..." : "Create user"}
      </button>

      {error && (
        <p className="user-feedback error-message" role="alert">
          {error}
        </p>
      )}
      {createdUser && (
        <p className="user-feedback success-message" role="status">
          User created: <strong>{createdUser.name}</strong> ({createdUser.email})
        </p>
      )}
    </form>
  );
}
