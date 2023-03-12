defmodule Gateway do
  @moduledoc false
  require Logger

  def get_token() do
    body = %{client_id: Application.fetch_env!(:auth_0, :client_id),
             client_secret: Application.fetch_env!(:auth_0, :client_secret),
             audience: Application.fetch_env!(:auth_0, :audience),
             grant_type: Application.fetch_env!(:auth_0, :grant_type)} |> Poison.encode!
    case HTTPoison.post(Application.fetch_env!(:auth_0, :token_url),body,%{"Content-Type": "application/json"}) do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        {:ok, data} = Poison.decode(body)
        IO.inspect data
        {:ok, data["access_token"]}
      {:error, err} ->
        {:error, err}
    end
  end

  def message_body(msg) do
    %{sender: msg["sender"],
      contents: msg["contents"],
      replies: msg["replies"],
      chatId: msg["chatId"],
      sentAt: msg["sentAt"],
      updatedAt: msg["updatedAt"],
      id: msg["id"],
    } |> Poison.encode!
  end

  def room_body(body) do
    %{teamId: body["teamId"],
      members: body["members"],
      teamName: body["teamName"],
    } |> Poison.encode!
  end

  def post_message(req, token) do
    body = message_body(req)
    headers = ["Authorization": "Bearer #{token}", "Content-Type": "application/json"]
    %HTTPoison.Response{status_code: status, body: res} =
      HTTPoison.post!("http://localhost:8180/api/messages/", body, headers)
      case status do
        201 -> res |> Poison.decode!
        405 -> {:error, "Could not post message"}
      end
  end
end
