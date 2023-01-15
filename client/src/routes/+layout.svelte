<script lang="ts">
	import '../app.css';
	import {onMount} from "svelte";
	import UserService from "$lib/service/userService";
	import {auth0Client, authUser, loadingAuth, userDB} from "$lib/stores/authStore";
	import AuthHeader from "$lib/components/AuthHeader.svelte";
	import {goto} from "$app/navigation";
	import type {LayoutServerData} from "../../.svelte-kit/types/src/routes/$types";

	export let data: LayoutServerData

	const { user, userData } = data
	console.log(user)
	console.log(userData)

	onMount(async () => {
		if((user && userData) && (!$authUser && !$userDB)) {
			userDB.set(userData)
			authUser.set(user)
		}

		if (window.location.href.includes('state=') && window.location.href.includes('code='))  {
			console.debug('Handling redirect callback and fetching user data')
			const res = await $auth0Client!.handleRedirectCallback()
			const data = await $auth0Client!.getUser()
			//user.set(data)
			if (!$userDB && user?.email !== undefined) {
				//await UserService.initializeUserData($auth0Client, data)
			}
			await goto(window.sessionStorage.getItem('lastPath') ? window.sessionStorage.getItem('lastPath') : '/home')
		}

	})

</script>
<div class="app bg-gray-100 text-textgray w-screen">
{#if !$loadingAuth}

	{#if user !== undefined}
		<AuthHeader user={user} />
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
