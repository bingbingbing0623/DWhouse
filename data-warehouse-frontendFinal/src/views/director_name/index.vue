<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12">
        <el-form ref="form" :model="form" label-width="120px" style="padding-top: 5vh">
          <el-row>
            <el-col :span="11">
              <el-form-item label="导演">
                <el-autocomplete v-model="form.director" :fetch-suggestions="directorSearchSuggest" placeholder="请输入导演名"
                                 style="width: 100%" clearable @select="handleSelect" size="small" />
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div style="text-align: center">
          <el-button type="primary" @click="search(form)" size="small" style="margin-left: 3vh" plain>查询</el-button>
        </div>
      </el-col>
      <el-col :span="1">
        <el-divider direction="vertical" />
      </el-col>
      <el-col :span="10">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="查询结果" name="search_res">
            <el-table :data="result" v-loading="isLoading" element-loading-text="正在为您查询..." stripe style="width: 100%" height="450">
              <el-table-column prop="movieTitle" label="标题" width="140" />
              <el-table-column prop="movieId" label="编号" width="115" />
              <el-table-column prop="date" label="上映日期" width="100" />
              <el-table-column prop="movieScore" label="评分" width="80" />
              <el-table-column prop="runtime" label="时长" width="80" />
              <el-table-column prop="genre" label="类型" width="120" />
              <el-table-column prop="edition" label="版本" width="80" />
              <el-table-column prop="positive" label="正面评价比例" width="80" />
              <el-table-column prop="format" label="格式" width="140" />
              <el-table-column prop="actors" label="演员" width="200" />
              <el-table-column prop="directors" label="导演" width="200" />
            </el-table>
            <el-row style="text-align: center; margin-top: 20px">
              <el-pagination layout="prev, pager, next, jumper" :current-page.sync="currentPage" :page-size="10"
                             :page-count="totalPage" @current-change="getNewPage(form)" small />
            </el-row>
          </el-tab-pane>
          <el-tab-pane label="耗时对比" name="speed">
            <div id="speed" style="width: 400px; height: 400px"></div>
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "DirectorSearch",
  data() {
    return {
      activeName: "search_res",
      isLoading: false,
      totalPage: 0,
      currentPage: 1,
      mysql_speed: 0,
      spark_speed: 0,
      form: {
        director: "",
        columns: ["directors"], // 默认查询导演
      },
      result: [],
    };
  },

  methods: {
    handleSelect(item) {
      console.log(item);
    },

    handleClick(tab, event) {
      console.log(tab, event);
    },

    directorSearchSuggest(queryString, cb) {
      //导演搜索建议
      this.$axios
          .get("/recommend/director", {
            params: {
              director: queryString,
              amount: 10,
            },
          })
          .then((res) => {
            var result = [];
            for (var i = 0; i < res.suggestions.length; i++) {
              result.push({value: res.suggestions[i]});
            }
            cb(result);
          })
          .catch((err) => {
            this.$message.error("当前网络异常，请稍后再试");
          });
    },

    search(form) {
      if (form.director.trim() === "") {
        this.$message.warning("请至少输入一个导演名!");
      } else {
        this.isLoading = true;

        // 调用数据库查询
        this.$axios
            .post("/movie/detail", {
              director: form.director,
              columns: form.columns,
              page: this.currentPage,
              per_page: 10,
            })
            .then((res) => {
              this.result = res.data;
              this.totalPage = res.pages;
              this.isLoading = false;
              this.mysql_speed = res.consuming_time;
            })
            .catch((err) => {
              this.$message.error("当前mysql网络异常，请稍后再试");
            });

        // 调用Hive查询
        this.$axios
            .post("/hive/movie/detail", {
              director: form.director,
              columns: form.columns,
              page: this.currentPage,
              per_page: 10,
            })
            .then((res) => {
              this.spark_speed = res.consuming_time;
            })
            .catch((err) => {
              this.$message.error("当前spark网络异常，请稍后再试");
            });
      }
    },

    getNewPage(form) {
      this.isLoading = true;
      this.$axios
          .post("/movie/detail", {
            director: form.director,
            columns: form.columns,
            page: this.currentPage,
            per_page: 10,
          })
          .then((res) => {
            this.result = res.data;
            this.mysql_speed = res.consuming_time;
            this.isLoading = false;
          })
          .catch((err) => {
            this.$message.error("当前mysql网络异常，请稍后再试");
          });

      this.$axios
          .post("/hive/movie/detail", {
            director: form.director,
            columns: form.columns,
            page: this.currentPage,
            per_page: 10,
          })
          .then((res) => {
            this.spark_speed = res.consuming_time;
          })
          .catch((err) => {
            this.$message.error("当前spark网络异常，请稍后再试");
          });
    },

    echartsInit() {
      this.$echarts.init(document.getElementById("speed")).setOption({
        title: {
          text: "查询耗时对比(s)",
        },
        tooltip: {},
        xAxis: {
          data: ["mysql", "hive"],
        },
        yAxis: {},
        series: [
          {
            name: "查询耗时(s)",
            type: "bar",
            data: [this.mysql_speed, this.spark_speed],
          },
        ],
      });
    },
  },
};
</script>

<style scoped>
.el-divider--vertical {
  height: 100vh;
}
</style>
