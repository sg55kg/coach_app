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
<div class="app bg-gray-100 text-textgray w-screen">
{#if !$loadingAuth}

	{#if $user !== undefined}
		<AuthHeader />
	{:else }
		<header class="mb-4 p-2 bg-gray-200 text-textgray flex justify-between align-middle py-4">
			<div class="flex items-center align-baseline">
				<a href="/" class="font-semibold tracking-widest uppercase text-2xl ml-3 text-yellow-lt">
					Coachable
				</a>
			</div>
		</header>
	{/if}

	<main class="w-screen m-0">
		<slot />
	</main>

<!--	<footer>-->
<!--		<p class="text-link underline">Contact Us</p>-->
<!--		<p>&nbsp;|&nbsp;</p>-->
<!--		<p class="text-link underline">About</p>-->
<!--	</footer>-->
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
		/*max-width: 64rem;*/
		margin: 0 auto;
		box-sizing: border-box;
	}

	footer {
		display: flex;
		flex-direction: row;
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
