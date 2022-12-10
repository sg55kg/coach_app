<script lang="ts">
	import '../app.css';
	import {onMount} from "svelte";
	import UserService from "../lib/service/userService";
	import {auth0Client, user, loadingAuth} from "../lib/stores/authStore";
	import AuthHeader from "../lib/components/AuthHeader.svelte";


	onMount(async () => {
		if($auth0Client === null) {
			console.log('fireeddd')
			await UserService.initializeAuth0Client()
		}
	})

</script>
<div class="app bg-gray-100 text-textgray">
{#if !$loadingAuth}

	{#if $user !== undefined}
		<AuthHeader />
	{:else }
		<header class="mb-4 p-2 bg-yellow-300">
			Global header for coach app
		</header>
	{/if}

	<main>
		<slot />
	</main>

	<footer class="text-textblue">
		<p>Global footer for coach app</p>
	</footer>
	{:else}
	<h1>Loading</h1>
{/if}

</div>

<style>
	.app {
		display: flex;
		flex-direction: column;
		min-height: 100vh;
	}

	main {
		flex: 1;
		display: flex;
		flex-direction: column;
		width: 100%;
		max-width: 64rem;
		margin: 0 auto;
		box-sizing: border-box;
	}

	footer {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 12px;
	}

	footer a {
		font-weight: bold;
	}

	@media (min-width: 480px) {
		footer {
			padding: 12px 0;
		}
	}
</style>
