

<script lang="ts">
	import '../app.css';
	import {authUser, isMobile, loadingAuth, userDB} from "$lib/stores/authStore";
	import type {LayoutServerData} from "../../.svelte-kit/types/src/routes/$types";
	import {User} from "$lib/classes/user";
	import {onMount} from "svelte";
	import {goto} from "$app/navigation";
	import LoadingSpinner from "$lib/components/shared/loading/LoadingSpinner.svelte";

	export let data: LayoutServerData

	if (data.user && data.userData) {
		$userDB = User.build(data.userData)
		$authUser = data.user
	} else {
		$userDB = null
		$authUser = undefined
	}
	let deferredPrompt;
	onMount(async () => {
		window.addEventListener("beforeinstallprompt", (e) => {
			// Prevent Chrome 67 and earlier from automatically showing the prompt
			e.preventDefault();
			// Stash the event so it can be triggered later.
			deferredPrompt = e;
			const addBtn = document.getElementById('add-btn')
			// Update UI to notify the user they can add to home screen
			addBtn.classList.remove('hidden')
			//addBtn.style.display = "block";

			addBtn.addEventListener("click", (e) => {
				// hide our user interface that shows our A2HS button
				addBtn.style.display = "none";
				// Show the prompt
				deferredPrompt.prompt();
				// Wait for the user to respond to the prompt
				deferredPrompt.userChoice.then((choiceResult) => {
					if (choiceResult.outcome === "accepted") {
						console.log("User accepted the A2HS prompt");
					} else {
						console.log("User dismissed the A2HS prompt");
					}
					deferredPrompt = null;
				});
			});
		});

		if ('serviceWorker' in navigator) {
			await navigator.serviceWorker.register('/service-worker.js', {
				type: import.meta.env.VITE_ENV === 'Dev' ? 'module' : 'classic'
			});
		}


		const mobileDevices  = [
			/Android/i,
			/webOS/i,
			/iPhone/i,
			/iPad/i,
			/iPod/i,
			/BlackBerry/i,
			/Windows Phone/i
		]

		if (mobileDevices.some(d => navigator.userAgent.match(d))) {
			$isMobile = true
			console.log($isMobile)
		}

		if ((($userDB && $authUser) || (data.user && data.userData)) && window.location.pathname === '/') {
			console.log('Found')
			await goto('/home')
		} else if (!$userDB && !data.user && window.location.pathname !== '/') {
			await goto('/')
		}

	})

</script>

<div class="app bg-gray-100 text-textgray w-screen">
{#if !$loadingAuth}

	{#if !$authUser}
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
	{:else}
	<div class="w-screen h-screen flex items-center justify-center">
		<LoadingSpinner spinnerColor="fill-yellow" width="16" height="16" />
	</div>
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
