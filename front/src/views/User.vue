<template>
  <div>
    <div v-if="$store.state.user.auth == 'ROLE_USER'"><UserInfo /></div>
    <div v-if="$store.state.user.auth == 'ROLE_ADMIN'"><AdminInfo /></div>
  </div>
</template>

<script>
import http from "@/api//http-common.js";
import UserInfo from "@/components/UserInfo";
import AdminInfo from "@/components/AdminInfo";
export default {
  components: { UserInfo, AdminInfo },
  data() {
    return {};
  },
  mounted() {
    const jwt = module.require("jsonwebtoken");
    let decodedAccessToken = jwt.decode(this.$store.state.token);

    this.$store.commit("userInfo", {
      id: decodedAccessToken.sub,
      auth: decodedAccessToken.role[0],
    });

    // http
    //   .get("/api/test", {
    //     headers: {
    //       Authorization: this.$store.state.token,
    //     },
    //   })
    //   .then((result) => {
    //     console.log(result.data);
    //   });
  },
};
</script>

<style>
</style>