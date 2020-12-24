import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {AnalyzeComponent} from './analyze/analyze.component';

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Results'
    },
    children: [
      {
        path: '',
        redirectTo: '/projects-menu',
        pathMatch: 'full'
      },
      {
        path: ':id',
        data: {
          breadcrumb: null
        },
        component: AnalyzeComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ResultsRoutingModule {
}
