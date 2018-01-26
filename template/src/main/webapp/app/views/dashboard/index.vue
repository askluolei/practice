<template>
  <div class="dashboard-container">
    <adminDashboard></adminDashboard>
  </div>
</template>

<script>
import adminDashboard from './admin'

export default {
  name: 'dashboard',
  components: { adminDashboard }
}
</script>
