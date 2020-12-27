import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ViewComponent} from "./view/view.component";
import {TestCaseResultTableComponent} from "./test-case-result-table/test-case-result-table.component";

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
      },
      {
        path: 'results',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/projects-menu/results/results.module#ResultsModule'
      },
      {
        path: 'table/:id',
        data: {
          breadcrumb: 'Table'
        },
        component: TestCaseResultTableComponent
      },
      {
        path: 'table',
        data: {
          breadcrumb: 'Table'
        },
        component: TestCaseResultTableComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ProjectsMenuRoutingModule { }
