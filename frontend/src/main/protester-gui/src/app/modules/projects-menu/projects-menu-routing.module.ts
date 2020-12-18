import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ViewComponent} from "./view/view.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Projects menu'
    },
    children: [
      {
        path: '',
        data: {
          breadcrumb: null
        },
        component: ViewComponent
      },
      {
        path: 'projects',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/projects-menu/projects/projects.module#ProjectsModule'
      },
      {
        path: 'datasets',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/projects-menu/datasets/datasets.module#DatasetsModule'
      },
      {
        path: 'scenarios',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/projects-menu/test-scenario/test-scenario.module#TestScenarioModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectsMenuRoutingModule { }
