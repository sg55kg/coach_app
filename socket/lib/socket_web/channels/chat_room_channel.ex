defmodule SocketWeb.ChatRoomChannel do
  use SocketWeb, :channel
  require Logger
  import Gateway

  @impl true
  def join("room:" <> _chat_id, payload, socket) do
    Logger.info "Received request to join chatroom"
    # moved get token to authorized function?
    if authorized? payload do
      case Gateway.get_token do
        {:ok, token} ->
          socket = assign(socket, :access_token, token)
          IO.inspect socket
          {:ok, socket}
        {:error, err} ->
          Logger.info "Failed to get token"
          {:error, %{reason: "unauthorized"}}
      end
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
    IO.inspect payload
    broadcast! socket, "typing", payload
    {:reply, {:ok, payload}, socket}
  end

  def handle_in("new:msg", msg, socket) do
    new_msg = Gateway.post_message(msg, socket.assigns.access_token)
#    new_msg = %{id: msg["id"], sender: msg["sender"], contents: msg["contents"], chatId: msg["chatId"], sentAt: msg["sentAt"], updatedAt: msg["updatedAt"], replies: msg["replies"]}
    broadcast! socket, "new:msg", new_msg
    {:reply, {:ok, %{msg: msg["contents"]}}, assign(socket, :user, msg["user"])}

  end

end
