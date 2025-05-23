<template>
  <Menubar :model="computedMenuItems" class="border-none">
    <template #start>
      <div class="flex items-center gap-4">
        <img 
          width="35"
          height="40"
          viewBox="0 0 35 40"
          fill="none"
          src="@/assets/boardr-logo.png" 
          alt="Boardr Logo" 
          class="h-8 w-auto mr-12"
        >
      </div>
      <span class="text-xl font-semibold">Boardr<span class="text-primary"></span> </span>
    </template>
    <template #end>
      <div class="flex items-center gap-2">
        <Button
          v-if="true"
          :icon="isDarkMode ? 'pi pi-sun' : 'pi pi-moon'"
          class="p-button-text end-action-button"
          @click="toggleDarkMode"
          aria-label="Toggle Theme"
        />
        <Button
          v-if="isLoggedIn"
          :label="'Welcome, ' + username + '!'"
          class="p-button-text end-action-button"
          @click="$router.push('/account')"
          aria-label="Account"
        />
        <Button
          v-if="isLoggedIn"
          label="Logout"
          icon="pi pi-sign-out"
          class="p-button-text end-action-button"
          @click="handleLogout"
          aria-label="Logout"
        />
        <Button
          v-else
          label="Login"
          icon="pi pi-sign-in"
          class="p-button-text end-action-button"
          @click="$router.push('/login')"
          aria-label="Login"
        />
      </div>
    </template>
  </Menubar>
</template>

<script>
import Menubar from 'primevue/menubar'
import Button from 'primevue/button'

export default {
  name: 'MenuBarItem',
  components: { Menubar, Button },
  props: {
    items: {
      type: Array,
      required: true,
    },
    isLoggedIn: {
      type: Boolean,
      default: false,
    },
    username: {
      type: String,
      default: 'User',
    },
    isDarkMode: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    computedMenuItems() {
      return this.items.map(item => ({
        label: item.label,
        icon: item.icon,
        command: item.command,
        items: item.subItems
          ? item.subItems.map(sub => ({
              label: sub.label,
              icon: sub.icon,
              command: sub.command,
            }))
          : undefined,
      }))
    },
  },
  methods: {
    handleLogout() {
      this.$emit('logout')
    },
    toggleDarkMode() {
      this.$emit('toggle-dark-mode')
    },
  },
}
</script>

<style scoped>
:deep(.p-menubar) {
  background: #181818;
  border: none;
  border-radius: 0;
}

:deep(.p-menuitem-link) {
  color: #e0e0e0;
  transition: color 0.2s ease;
}

:deep(.p-menuitem-link:hover) {
  color: #0d8aec;
  background: rgba(255, 255, 255, 0.1);
}

:deep(.p-submenu-list) {
  background: #181818;
  border: none;
}

.end-action-button {
  color: v-bind('isDarkMode ? "#e0e0e0" : "#1a1a1a"'); /* Dark text in light mode, light text in dark mode */
  transition: color 0.2s ease;
}

.end-action-button:hover {
  color: #0d8aec; /* Matches .p-menuitem-link:hover */
  background: rgba(255, 255, 255, 0.1); /* Matches .p-menuitem-link:hover */
}

:deep(.p-button-text) {
  text-transform: none; /* Prevents uppercase transformation */
}
</style>