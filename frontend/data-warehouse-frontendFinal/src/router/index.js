import Vue from "vue";
import Router from "vue-router";

Vue.use(Router);

/* Layout */
import Layout from "@/layout";

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: "/404",
    component: () => import("@/views/404"),
    hidden: true,
  },

  // 如果你有其他页面，也可以继续添加
  {
    path: "/",
    component: Layout,
    meta: { title: "查询", icon: "search" },
    redirect: "/relation-search",
    children: [
      {
        path: "/complex-search",
        component: () => import("@/views/complex_search/index"),
        meta: { title: "按时间查询" },
      },
      {
        path: "/movie-name",
        component: () => import("@/views/movie_name/index"),
        meta: { title: "按电影名称查询" },
      },
      {
        path: "/director-name",
        component: () => import("@/views/director_name/index"),
        meta: { title: "按导演查询" },
      },
      {
        path: "/actor-search",
        component: () => import("@/views/actor_search/index"),
        meta: { title: "按演员查询" },
      },
      {
        path: "/genre-search",
        component: () => import("@/views/genre_search/index"),
        meta: { title: "按电影类别查询" },
      },
      {
        path: "/evaluate-search",
        component: () => import("@/views/evaluate_search/index"),
        meta: { title: "按用户评价查询" },
      },
      {
        path: "/relation-search",
        component: () => import("@/views/relation_search/index"),
        meta: { title: "关系查询" },
      },
    ],
  },

  // 404 page must be placed at the end !!!
  { path: "*", redirect: "/404", hidden: true },
];

const createRouter = () =>
    new Router({
      // mode: 'history', // 需要服务支持，如果你希望使用 history 模式
      scrollBehavior: () => ({ y: 0 }),
      routes: constantRoutes,
    });

const router = createRouter();

// Reset router if needed (in case you want to change routes dynamically)
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
