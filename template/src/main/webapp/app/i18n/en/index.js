import audits from './audits'
import configuration from './configuration'
import entityAudit from './entityAudit'
import health from './health'
import logs from './logs'
import metrics from './metrics'
import route from './route'
import tracker from './tracker'
import common from './common'

export default {
  ...audits,
  ...configuration,
  ...entityAudit,
  ...health,
  ...logs,
  ...metrics,
  ...route,
  ...tracker,
  ...common
}
