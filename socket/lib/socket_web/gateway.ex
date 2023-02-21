defmodule Gateway do
  @moduledoc false
  require Logger

  def get_token() do
    body = %{client_id: Application.fetch_env!(:auth_0, :client_id),
             client_secret: Application.fetch_env!(:auth_0, :client_secret),
             audience: Application.fetch_env!(:auth_0, :audience),
             grant_type: Application.fetch_env!(:auth_0, :grant_type)} |> Poison.encode!
    case HTTPoison.post("https://dev-iubbkos4gue16ad5.us.auth0.com/oauth/token",body,%{"Content-Type": "application/json"}) do
      {:ok, %HTTPoison.Response{status_code: 200, body: body}} ->
        {:ok, data} = Poison.decode(body)
        IO.inspect data
        {:ok, data["access_token"]}
      {:error, err} ->
        {:error, err}
    end
  end

  def message_body(msg) do
    %{senderId: msg["senderId"],
      contents: msg["contents"],
      replies: msg["replies"],
      chatId: msg["chatId"],
      id: msg["id"],
      senderName: msg["senderName"]} |> Poison.encode!
  end

  def room_body(body) do
    %{teamId: body["teamId"],
      members: body["members"],
      teamName: body["teamName"],
    } |> Poison.encode!
  end

  def post_message(req, token) do
    body = message_body(req)
    IO.inspect body
    headers = ["Authorization": "Bearer #{token}", "Content-Type": "application/json"]
    res = HTTPoison.post!("http://localhost:8180/api/messages/", body, headers)
    IO.inspect res
#    token_response = get_token
#    case get_token() do
#      {:ok, token} ->
#       # res = HTTPoison.post!("http://localhost:8180/api/messages", body, %{"Content-Type": "application/json", "Authorization": "Bearer " <> token})
#       # IO.inspect res
#       IO.puts "Test"
#      {:error, err} ->
#        err
#    end

  end
end
