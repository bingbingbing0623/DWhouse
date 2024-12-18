<template>
  <div class="app-container">
    <el-row>
      <el-col :span="12">
        <el-form ref="form" :model="form" label-width="120px" style="padding-top: 5vh">
          <el-row>
            <el-col :span="11">
              <el-form-item label="电影名称">
                <el-autocomplete v-model="form.title" :fetch-suggestions="movieSearchSuggest" placeholder="请输入电影名"
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
            <el-table :data="result" v-loading="isLoading" element-loading-text="正在为您查询..." stripe style="width: 100%"
                      height="450">
              <el-table-column prop="movieTitle" label="标题" width="140" v-if="columns.title" />
              <el-table-column prop="movieId" label="编号" width="115" v-if="columns.movieId" />
              <el-table-column prop="date" label="上映日期" width="100" v-if="columns.date" />
              <el-table-column prop="movieScore" label="评分" width="80" v-if="columns.score" />
              <el-table-column prop="runtime" label="时长" width="80" v-if="columns.runtime" />
              <el-table-column prop="genre" label="类型" width="120" v-if="columns.genre" />
              <el-table-column prop="edition" label="版本" width="80" v-if="columns.edition" />
              <el-table-column prop="positive" label="正面评价比例" width="80" v-if="columns.positive" />
              <el-table-column prop="format" label="格式" width="140" v-if="columns.format" />
              <el-table-column prop="actors" label="演员" width="200" v-if="columns.actors" />
              <el-table-column prop="directors" label="导演" width="200" v-if="columns.directors" />
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
  name: "ComplexSearch",
  data() {
    return {
      activeName: "search_res",
      isLoading: false,
      totalPage: 0,
      currentPage: 1,
      mysql_speed: 0,
      spark_speed: 0,
      form: {
        min_score: 0,
        max_score: 5.0,
        year: null,
        month: null,
        season: null,
        day: null,
        weekday: null,
        title: "",
        genre: "",
        director: "",
        actor: "",
        columns: [],
        month_season: "",
        day_weekday: "",
      },
      columns: {
        title: false,
        score: false,
        edition: false,
        format: false,
        date: false,
        directors: false,
        actors: false,
      },
      result: [],
      test: "",
    };
  },

  mounted() {
    this.echartsInit();
  },

  watch: {
    mysql_speed: {
      handler(newValue) {
        this.mysql_speed = newValue;
        this.echartsInit();
      },
    },
    spark_speed: {
      handler(newValue) {
        this.spark_speed = newValue;
        this.echartsInit();
      },
    },
  },

  methods: {
    handleSelect(item) {
      console.log(item);
    },

    handleClick(tab, event) {
      console.log(tab, event);
    },

    movieSearchSuggest(queryString, cb) {
      this.$axios
          .get("/recommend/movie", {
            params: {
              title: queryString,
              amount: 10,
            },
          })
          .then((res) => {
            const result = res.suggestions.map(item => ({ value: item }));
            cb(result);
          })
          .catch(() => {
            this.$message.error("当前网络异常，请稍后再试");
          });
    },

    genreSearchSuggest(queryString, cb) {
      this.$axios
          .get("/recommend/genre", {
            params: {
              genre: queryString,
              amount: 10,
            },
          })
          .then((res) => {
            const result = res.suggestions.map(item => ({ value: item }));
            cb(result);
          })
          .catch(() => {
            this.$message.error("当前网络异常，请稍后再试");
          });
    },

    directorSearchSuggest(queryString, cb) {
      this.$axios
          .get("/recommend/director", {
            params: {
              director: queryString,
              amount: 10,
            },
          })
          .then((res) => {
            const result = res.suggestions.map(item => ({ value: item }));
            cb(result);
          })
          .catch(() => {
            this.$message.error("当前网络异常，请稍后再试");
          });
    },

    actorSearchSuggest(queryString, cb) {
      this.$axios
          .get("/recommend/actor", {
            params: {
              actor: queryString,
              amount: 10,
            },
          })
          .then((res) => {
            const result = res.suggestions.map(item => ({ value: item }));
            cb(result);
          })
          .catch(() => {
            this.$message.error("当前网络异常，请稍后再试");
          });
    },

    search(form) {
      if (form.columns.length === 0) {
        this.$message.warning("请至少输入一个条件!");
      } else {
        this.isLoading = true;

        if (form.year === 1930) {
          form.year = null;
        }

        if (form.month_season === "") {
          form.month = null;
          form.season = null;
        } else if (form.month_season === "month") {
          form.season = null;
        } else if (form.month_season === "season") {
          form.month = null;
        }

        if (form.day_weekday === "") {
          form.day = null;
          form.weekday = null;
        } else if (form.day_weekday === "day") {
          form.weekday = null;
        } else if (form.day_weekday === "weekday") {
          form.day = null;
        }

        for (let column in this.columns) {
          this.columns[column] = false;
        }

        for (let i = 0; i < form.columns.length; i++) {
          this.columns[form.columns[i]] = true;
        }

        this.$axios
            .post("/movie/count", {
              genre_name: form.genre,
              min_score: form.min_score,
              max_score: form.max_score,
              title: form.title,
              director: form.director,
              actor: form.actor,
              year: form.year,
              month: form.month,
              season: form.season,
              weekday: form.weekday,
              day: form.day,
              positive: form.positive / 100,
              page: 1,
              per_page: 10,
            })
            .then((res) => {
              this.totalPage = res.pages;
            });

        this.$axios
            .post("/movie/detail", {
              genre_name: form.genre,
              min_score: form.min_score,
              max_score: form.max_score,
              columns: form.columns,
              title: form.title,
              director: form.director,
              actor: form.actor,
              year: form.year,
              month: form.month,
              season: form.season,
              weekday: form.weekday,
              day: form.day,
              positive: form.positive / 100,
              page: 1,
              per_page: 10,
            })
            .then((res) => {
              this.result = res.data;
              if (this.columns["title"]) {
                this.result.forEach((movie) => {
                  movie.movieTitle = movie.movieTitle.replace(/\"/g, "");
                });
              }
              this.isLoading = false;
              this.mysql_speed = res.consuming_time;
            })
            .catch(() => {
              this.$message.error("当前mysql网络异常，请稍后再试");
            });

        this.$axios
            .post("/hive/movie/detail", {
              genre_name: form.genre,
              min_score: form.min_score,
              max_score: form.max_score,
              columns: form.columns,
              title: form.title,
              director: form.director,
              actor: form.actor,
              year: form.year,
              month: form.month,
              season: form.season,
              weekday: form.weekday,
              day: form.day,
              positive: form.positive / 100,
              page: 1,
              per_page: 10,
            })
            .then((res) => {
              this.result = res.data;
              if (this.columns["title"]) {
                this.result.forEach((movie) => {
                  movie.movieTitle = movie.movieTitle.replace(/\"/g, "");
                });
              }
              this.isLoading = false;
              this.spark_speed = res.consuming_time;
            })
            .catch(() => {
              this.$message.error("当前hive网络异常，请稍后再试");
            });
      }
    },

    getNewPage(form) {
      this.isLoading = true;
      this.$axios
          .post("/movie/detail", {
            genre_name: form.genre,
            min_score: form.min_score,
            max_score: form.max_score,
            columns: form.columns,
            title: form.title,
            director: form.director,
            actor: form.actor,
            year: form.year,
            month: form.month,
            season: form.season,
            weekday: form.weekday,
            day: form.day,
            positive: form.positive / 100,
            page: this.currentPage,
            per_page: 10,
          })
          .then((res) => {
            this.result = res.data;
            this.isLoading = false;
          });
    },

    echartsInit() {
      this.$echarts.init(document.getElementById("speed")).setOption({
        title: {
          text: "组合查询耗时对比(s)",
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
.app-container {
  padding: 20px;
}
</style>
