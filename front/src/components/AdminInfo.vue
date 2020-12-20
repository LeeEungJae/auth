<template>
  <div>
    <div><h1 class="text-center">Admin 페이지 입니다.</h1></div>
    <table
      class="table table-hover table-striped text-center"
      style="border: 1px soild"
    >
      <thead>
        <tr>
          <th>번호</th>
          <th>이메일</th>
          <th>탈퇴</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="info in user" :key="info.email">
          <th>{{ info.code }}</th>
          <th>{{ info.email }}</th>
          <th><i class="fa fa-trash" @click="deleteUser(info.email)"></i></th>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import http from "@/api//http-common.js";
export default {
  data() {
    return { user: [] };
  },
  mounted() {
    http
      .get("/api/user", {
        headers: {
          Authorization: this.$store.state.token,
        },
      })
      .then((result) => {
        this.user = result.data;
        console.log(this.user);
      });
  },
  methods: {
    deleteUser(param) {
      http
        .delete("/admin/user", {
          headers: {
            Authorization: this.$store.state.token,
          },
          params: {
            email: param,
          },
        })
        .then((result) => {
          console.log(result.data);
          if (result.data.success != 0) {
            http
              .get("/api/user", {
                headers: {
                  Authorization: this.$store.state.token,
                },
              })
              .then((result) => {
                this.user = [];
                this.user = result.data;
                console.log(this.user);
              });
          }
        });
    },
  },
};
</script>

<style>
</style>