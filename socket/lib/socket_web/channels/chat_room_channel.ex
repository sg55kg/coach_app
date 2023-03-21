defmodule SocketWeb.ChatRoomChannel do
  use SocketWeb, :channel
  require Logger
  import Gateway

  @impl true
  def join("room:" <> _chat_id, payload, socket) do
    Logger.info "Received request to join chatroom"
    # moved get token to authorized function?
    if authorized? payload do
      socket = assign(socket, :access_token, payload)
      {:ok, socket}
#      case Gateway.get_token do
#        {:ok, token} ->
#          socket = assign(socket, :access_token, token)
#          {:ok, socket}
#        {:error, err} ->
#          Logger.info "Failed to get token"
#          {:error, %{reason: "unauthorized"}}
#      end
    else
      {:error, %{reason: "unauthorized"}}
    end
  end

  # Channels can be used in a request/response fashion
  # by sending replies to requests from the client
  @impl true
  def handle_in("ping", payload, socket) do
    Logger.info "Joining ping"
    {:reply, {:ok, payload}, socket}
  end

  # It is also common to receive messages from the client and
  # broadcast to everyone in the current topic (chat_room:lobby).
  @impl true
  def handle_in("shout", payload, socket) do
    Logger.info "Joining shout"
    broadcast(socket, "shout", payload)
    {:noreply, socket}
  end

  # Add authorization logic here as required.
  defp authorized?(_payload) do
    true
  end

  @impl true
  def handle_in("typing", payload, socket) do
    broadcast! socket, "typing", payload
    {:reply, {:ok, payload}, socket}
  end

  def handle_in("new:msg", msg, socket) do
    new_msg = Gateway.post_message(msg, socket.assigns.access_token)
    case new_msg do
      {:ok, msg} ->
        broadcast! socket, "new:msg", msg
        {:reply, {:ok, %{msg: msg["contents"]}}, assign(socket, :user, msg["user"])}
      {:error, msg} ->
        {:reply, :error, socket}
    end
  end

#  def handle_in("upload:media", payload, socket) do
#    Logger.info "Received request to upload media"
##    IO.inspect payload["binary"]
#    encoded = Base.decode64!(payload["binary"])
#    res = Gateway.upload_media(encoded, payload["filename"])
#    case res do
#      {:ok, body} ->
#        broadcast! socket, "upload:media", res
#        {:reply, :ok, socket}
#      {:error, msg} ->
#        {:reply, :error, socket}
#    end
#
##    {:reply, :ok, socket}
#  end

end
