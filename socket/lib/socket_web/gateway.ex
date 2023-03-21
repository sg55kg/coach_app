defmodule Gateway do
  @moduledoc false
  require Logger

  def get_token() do
    body = %{client_id: Application.fetch_env!(:socket, :client_id),
             client_secret: Application.fetch_env!(:socket, :client_secret),
             audience: Application.fetch_env!(:socket, :audience),
             grant_type: Application.fetch_env!(:socket, :grant_type)} |> Poison.encode!
    url = Application.fetch_env!(:socket, :token_url)
    case HTTPoison.post(url,body,%{"Content-Type": "application/json"}) do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        {:ok, data} = Poison.decode(body)
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
    server_url = Application.fetch_env!(:socket, :audience) <> "/api/messages/"
    %HTTPoison.Response{status_code: status, body: res} =
      HTTPoison.post!(server_url, body, headers)
      case status do
        201 -> {:ok, res |> Poison.decode!}
        405 -> {:error, res |> Poison.decode!}
        500 -> {:error, res |> Poison.decode!}
      end
  end

#  def upload_media(payload, filename) do
#    body = %{file: payload, filename: filename} |> Poison.encode!
#    stream_token = Application.fetch_env!(:socket, :stream_token)
#    cf_url = Application.fetch_env!(:socket, :stream_url)
#    headers = ["Authorization": "Bearer #{stream_token}", "Content-Type": "multipart/form-data"]
#    %HTTPoison.Response{status_code: status, body: res} =
#      HTTPoison.post!(cf_url, body, headers)
#      case status do
#        201 -> {:ok, res |> Poison.decode!}
#        200 -> {:ok, res |> Poison.decode!}
#        405 -> {:error, res |> Poison.decode!}
#      end
#  end
end
