import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {ViewComponent} from "./view/view.component";

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        component: ViewComponent
      },
      {
        path: 'projects',
        loadChildren: 'src/app/projects-menu/projects/projects.module#ProjectsModule'
      },
      {
        path: 'datasets',
        loadChildren: 'src/app/projects-menu/datasets/datasets.module#DatasetsModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectsMenuRoutingModule { }
